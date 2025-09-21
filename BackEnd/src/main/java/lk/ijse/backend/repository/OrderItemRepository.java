package lk.ijse.backend.repository;

import lk.ijse.backend.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {

    @Query("SELECT oi FROM OrderItems oi WHERE oi.order.orderId = :orderId")
    List<OrderItems> findOrderItemByOrderId(@Param("orderId") Long orderId);




}
