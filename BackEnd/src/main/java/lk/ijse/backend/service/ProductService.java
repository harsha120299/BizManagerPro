package lk.ijse.backend.service;

import lk.ijse.backend.dto.ProductDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    String addProduct(ProductDTO productDTO);
    String updateProduct(ProductDTO productDTO);
    String deleteProduct(Long productId);
    List<ProductDTO> getAllProducts(long id);
    List<ProductDTO> getProductById(Long id, Long businessId);

    Page<ProductDTO> getAllProductsPaginated(Long businessId, int page, int size, String search);

    List<ProductDTO> getAllProductsList(Long businessId);

    Page<ProductDTO> searchProductsByBusiness(Long businessId, String search, int page, int size);
}
