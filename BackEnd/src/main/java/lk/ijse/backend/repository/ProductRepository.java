package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Material;
import lk.ijse.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.business.businessId = :businessId")
    Optional<Product> findByNameAndBusinessId(@Param("name") String name,
                                              @Param("businessId") Long businessId);
    
    @Query("SELECT p FROM Product p WHERE p.productId = :productId AND p.business.businessId = :businessId")
    List<Product> findProductByIdAndBusinessId(@Param("productId") Long productId, @Param("businessId") Long businessId);

    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId")
    Page<Product> findByBusinessId(@Param("businessId") Long businessId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> findByBusinessIdAndNameContainingIgnoreCase(@Param("businessId") Long businessId,
                                                              @Param("search") String search,
                                                              Pageable pageable);

    @Query("""
       SELECT DISTINCT p 
       FROM Product p
       LEFT JOIN FETCH p.productMaterials pm
       LEFT JOIN FETCH pm.material m
       WHERE p.business.businessId = :businessId
       """)
    List<Product> findProductByBusinessIdWithMaterials(@Param("businessId") Long businessId);


    @Query("SELECT p FROM Product p WHERE p.business.businessId = :businessId")
    List<Product> findByBusinessId(@Param("businessId") Long businessId);

}
