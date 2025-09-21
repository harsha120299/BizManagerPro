package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByName(String name);

    @Query("SELECT m FROM Material m WHERE m.business.businessId = :businessId")
    List<Material> findMaterialsByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT m FROM Material m WHERE m.business.businessId = :businessId AND LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Material> findByBusinessIdAndNameContainingIgnoreCase(
            @Param("businessId") Long businessId,
            @Param("search") String search,
            Pageable pageable
    );
    @Query("SELECT m FROM Material m WHERE m.business.businessId = :businessId")
    Page<Material> findByBusinessId(@Param("businessId") Long businessId, Pageable pageable);

}
