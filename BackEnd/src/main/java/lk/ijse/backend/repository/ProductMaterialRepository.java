package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Material;
import lk.ijse.backend.entity.Product;
import lk.ijse.backend.entity.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {
    void deleteAllByProduct(Product product);
    List<ProductMaterial> findAllByProduct(Product product);
    ProductMaterial findByProductAndMaterial(Product product, Material material);
}

