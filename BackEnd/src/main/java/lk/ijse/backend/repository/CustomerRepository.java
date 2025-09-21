package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.name = :name AND c.business.businessId = :businessId")
    Optional<Customer> findCustomersByNameAndBusinessId(@Param("name") String name, @Param("businessId") long businessId);

    @Query("SELECT c FROM Customer c WHERE c.business.businessId = :businessId")
    List<Customer> findAllCustomerByBusinessId(@Param("businessId") Long businessId);
    @Query("SELECT c FROM Customer c WHERE c.business.businessId = :businessId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> findByBusinessIdAndNameContainingIgnoreCase(@Param("businessId") Long businessId,
                                                               @Param("name") String name,
                                                               Pageable pageable);
}
