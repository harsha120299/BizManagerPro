package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.ProductDTO;
import lk.ijse.backend.dto.ProductMaterialDTO;
import lk.ijse.backend.entity.*;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.BusinessRepository;
import lk.ijse.backend.repository.MaterialRepository;
import lk.ijse.backend.repository.ProductMaterialRepository;
import lk.ijse.backend.repository.ProductRepository;
import lk.ijse.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository PRODUCT_REPOSITORY;
    private final BusinessRepository BUSINESS_REPOSITORY;
    private final MaterialRepository MATERIAL_REPOSITORY;
    private final ProductMaterialRepository PRODUCT_MATERIAL_REPOSITORY;
    private final ModelMapper MODEL_MAPPER;

    @Override
    @Transactional
    public String addProduct(ProductDTO productDTO) {
        if (PRODUCT_REPOSITORY.findByNameAndBusinessId(productDTO.getName(), productDTO.getBusinessId()).isPresent()) {
            throw new RuntimeException("Product name already exists: " + productDTO.getName());
        }

        Business business = BUSINESS_REPOSITORY.findById(productDTO.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business"));

        Product product = Product.builder()
                .name(productDTO.getName())
                .defaultPrice(productDTO.getPrice())
                .quantity(productDTO.getQuantity())
                .business(business)
                .build();

        Product savedProduct = PRODUCT_REPOSITORY.save(product);

        if (productDTO.getProductMaterials() != null && !productDTO.getProductMaterials().isEmpty()) {
            for (ProductMaterialDTO productMaterialDTO : productDTO.getProductMaterials()) {
                Material material = MATERIAL_REPOSITORY.findById(productMaterialDTO.getMaterialId())
                        .orElseThrow(() -> new ResourceNotFoundException("Material"));

                double perUnitQty = productMaterialDTO.getQuantityUsed() != null ? productMaterialDTO.getQuantityUsed() : 0;
                double productQty = productDTO.getQuantity();
                double totalUsedQty = perUnitQty * productQty;

                double currentStock = material.getQuantity() != null ? material.getQuantity() : 0;
                if (totalUsedQty > currentStock) {
                    throw new RuntimeException("Not enough stock for material: " + material.getName());
                }

                material.setQuantity(currentStock - totalUsedQty);
                MATERIAL_REPOSITORY.save(material);

                ProductMaterial productMaterial = ProductMaterial.builder()
                        .product(savedProduct)
                        .material(material)
                        .quantityUsed(perUnitQty)
                        .build();

                PRODUCT_MATERIAL_REPOSITORY.save(productMaterial);
            }
        }

        return "Product added successfully";
    }



    @Override
    @Transactional
    public String updateProduct(ProductDTO productDTO) {
        Product product = PRODUCT_REPOSITORY.findById(productDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product"));

        double oldQty = product.getQuantity();
        double newQty = productDTO.getQuantity();
        double diffQty = newQty - oldQty;

        product.setName(productDTO.getName());
        product.setDefaultPrice(productDTO.getPrice());
        product.setQuantity(newQty);
        PRODUCT_REPOSITORY.save(product);

        if (productDTO.getProductMaterials() != null && !productDTO.getProductMaterials().isEmpty()) {
            for (ProductMaterialDTO productMaterialDTO : productDTO.getProductMaterials()) {
                Material material = MATERIAL_REPOSITORY.findById(productMaterialDTO.getMaterialId())
                        .orElseThrow(() -> new ResourceNotFoundException("Material"));

                double perUnitUsed = productMaterialDTO.getQuantityUsed() != null ? productMaterialDTO.getQuantityUsed() : 0;
                double currentMaterialQty = material.getQuantity() != null ? material.getQuantity() : 0;

                if (diffQty > 0) {
                    double totalUsed = perUnitUsed * diffQty;
                    if (totalUsed > currentMaterialQty) {
                        throw new RuntimeException("Not enough stock for material: " + material.getName());
                    }
                    material.setQuantity(currentMaterialQty - totalUsed);
                } else if (diffQty < 0) {
                    double totalRestore = perUnitUsed * (-diffQty);
                    material.setQuantity(currentMaterialQty + totalRestore);
                }
                MATERIAL_REPOSITORY.save(material);

                ProductMaterial productMaterial = PRODUCT_MATERIAL_REPOSITORY.findByProductAndMaterial(product, material);
                if (productMaterial == null) {
                    productMaterial = ProductMaterial.builder()
                            .product(product)
                            .material(material)
                            .build();
                }

                productMaterial.setQuantityUsed(perUnitUsed);
                PRODUCT_MATERIAL_REPOSITORY.save(productMaterial);
            }
        }

        return "Product updated successfully";
    }


    @Override
    @Transactional
    public String deleteProduct(Long productId) {
        Product product = PRODUCT_REPOSITORY.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));

        PRODUCT_MATERIAL_REPOSITORY.deleteAllByProduct(product);
        PRODUCT_REPOSITORY.delete(product);

        return "Product deleted successfully";
    }

    @Override
    public List<ProductDTO> getAllProducts(long businessId) {
        List<Product> products = PRODUCT_REPOSITORY.findProductByBusinessIdWithMaterials(businessId);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return PRODUCT_REPOSITORY.findAll().stream().map(products1 -> MODEL_MAPPER.map(products1,ProductDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductById(Long id, Long businessId) {
        List<Product> products = PRODUCT_REPOSITORY.findProductByIdAndBusinessId(id, businessId);
        System.out.println(products);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return PRODUCT_REPOSITORY.findAll().stream().map(products1 -> MODEL_MAPPER.map(products1,ProductDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> getAllProductsPaginated(Long businessId, int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage;

        if (search != null && !search.isEmpty()) {
            productsPage = PRODUCT_REPOSITORY.findByBusinessIdAndNameContainingIgnoreCase(businessId, search, pageable);
        } else {
            productsPage = PRODUCT_REPOSITORY.findByBusinessId(businessId, pageable);
        }

        Page<ProductDTO> dtoPage = productsPage.map(product -> MODEL_MAPPER.map(product, ProductDTO.class));

        if (dtoPage.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }

        return dtoPage;
    }

    @Override
    public List<ProductDTO> getAllProductsList(Long businessId) {
        List<Product> products = PRODUCT_REPOSITORY.findByBusinessId(businessId);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found");
        }
        return products.stream()
                .map(product -> MODEL_MAPPER.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> searchProductsByBusiness(Long businessId, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = PRODUCT_REPOSITORY.findByBusinessIdAndNameContainingIgnoreCase(businessId, search, pageable);

        Page<ProductDTO> dtoPage = productsPage.map(product -> MODEL_MAPPER.map(product, ProductDTO.class));

        if (dtoPage.isEmpty()) {
            throw new ResourceNotFoundException("No products found for search: " + search);
        }

        return dtoPage;
    }

}
