package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBusiness_BusinessId(Long businessId);

}
