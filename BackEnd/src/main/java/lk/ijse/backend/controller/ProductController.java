package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.ProductDTO;
import lk.ijse.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService PRODUCT_SERVICE;

    // Add Product
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDTO productDTO) {
        System.out.println("Add Product: " + productDTO);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                PRODUCT_SERVICE.addProduct(productDTO)
        ));
    }

    // Get All Products (Paginated)
    @GetMapping("/getAll/{businessId}")
    public ResponseEntity<ApiResponse> getAllProductsPaginated(
            @PathVariable Long businessId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        System.out.println("getAllProductsPaginated: " + page);
        Page<ProductDTO> productsPage = PRODUCT_SERVICE.getAllProductsPaginated(businessId, page, size, search);
        return ResponseEntity.ok(new ApiResponse(200, "OK", productsPage));
    }

    // Get All Products (List, no pagination)
    @GetMapping("/getAllList/{businessId}")
    public ResponseEntity<ApiResponse> getAllProductsList(@PathVariable Long businessId) {
        List<ProductDTO> products = PRODUCT_SERVICE.getAllProductsList(businessId);
        return ResponseEntity.ok(new ApiResponse(200, "OK", products));
    }

    // Update Product
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDTO productDTO) {
        System.out.println("Update Product: " + productDTO);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                PRODUCT_SERVICE.updateProduct(productDTO)
        ));
    }

    // Delete Product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                PRODUCT_SERVICE.deleteProduct(id)
        ));
    }

    // Get Product by ID + Business ID
    @GetMapping("/get/{productId}/{businessId}")
    public ResponseEntity<ApiResponse> getProductById(
            @PathVariable Long productId,
            @PathVariable Long businessId
    ) {
        System.out.println("Get Product By ID");
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                PRODUCT_SERVICE.getProductById(productId, businessId)
        ));
    }
}
