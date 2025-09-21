package lk.ijse.backend.repository;

import lk.ijse.backend.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Search orders by customer name inside a business
    @Query("SELECT o FROM Order o WHERE o.customer.business.businessId = :businessId AND LOWER(o.customer.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Order> findByCustomerNameContainingAndCustomerBusinessId(@Param("name") String name,
                                                                  @Param("businessId") Long businessId,
                                                                  Pageable pageable);

    // Get all orders by business (non-paginated)
    @Query("SELECT o FROM Order o WHERE o.customer.business.businessId = :businessId")
    List<Order> findByCustomerBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT o FROM Order o WHERE o.business.businessId = :businessId ORDER BY o.orderDate DESC")
    Page<Order> findOrdersByBusinessId(@Param("businessId") Long businessId, Pageable pageable);


    // Fetch order with all items eagerly in a single query
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Order findOrderWithItems(@Param("orderId") Long orderId);


}
