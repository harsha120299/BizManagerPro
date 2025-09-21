package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Business;
import lk.ijse.backend.entity.UserBusiness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT b FROM Business b JOIN UserBusiness ub ON b.businessId = ub.id WHERE ub.id = :userId")
    List<Business> findBusinessesByUserId(@Param("userId") Long userId);
}
