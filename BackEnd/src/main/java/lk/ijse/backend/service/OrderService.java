package lk.ijse.backend.service;

import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.dto.OrderItemDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderDTO saveOrder(OrderDTO orderDTO);
    Page<OrderDTO> getAllOrdersByPages(Long businessId, int page, int size, String search);
    List<OrderDTO> getAllOrders(Long businessId);
    OrderDTO updateOrder(OrderDTO orderDTO);
    boolean deleteOrder(Long id);
    OrderDTO getOrderById(Long orderId);

}
