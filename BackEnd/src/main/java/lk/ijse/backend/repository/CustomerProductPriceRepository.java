package lk.ijse.backend.repository;

import aj.org.objectweb.asm.commons.Remapper;
import lk.ijse.backend.entity.Customer;
import lk.ijse.backend.entity.CustomerProductPrice;
import lk.ijse.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerProductPriceRepository extends JpaRepository<CustomerProductPrice,Long> {
    Remapper findByCustomerAndProduct(Customer customer, Product product);
}
