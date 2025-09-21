package lk.ijse.backend.controller;

import lk.ijse.backend.dto.ApiResponse;
import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.dto.OrderItemDTO;
import lk.ijse.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService ORDER_SERVICE;

    // Add Order
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody OrderDTO orderDTO) {
        System.out.println("add order: " + orderDTO);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                ORDER_SERVICE.saveOrder(orderDTO)
        ));
    }

    // Get Orders with Pagination + Search
    @GetMapping("/getAll/{id}")
    public ResponseEntity<ApiResponse> getAllOrdersPaginated(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        System.out.println("getAllOrdersPaginated: " + id);
        Page<OrderDTO> ordersPage = ORDER_SERVICE.getAllOrdersByPages(id, page, size, search);
        return ResponseEntity.ok(new ApiResponse(200, "OK", ordersPage));
    }

    // Get Orders List (for dropdowns)
    @GetMapping("/getAllList/{id}")
    public ResponseEntity<ApiResponse> getAllOrdersList(@PathVariable Long id) {
        System.out.println("getAllOrdersList " + id);
        List<OrderDTO> orders = ORDER_SERVICE.getAllOrders(id);
        System.out.println("getAllOrdersList " + orders);
        return ResponseEntity.ok(new ApiResponse(200, "OK", orders));
    }

    // Update Order
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateOrder(@RequestBody OrderDTO orderDTO) {
        System.out.println("update order: " + orderDTO);
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                ORDER_SERVICE.updateOrder(orderDTO)
        ));
    }

    // Delete Order
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(
                200,
                "OK",
                ORDER_SERVICE.deleteOrder(id)
        ));
    }
    @GetMapping("/get/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        OrderDTO orderDTO = ORDER_SERVICE.getOrderById(orderId);
        if (orderDTO == null) {
            return ResponseEntity.status(404).body("Order not found");
        }
        return ResponseEntity.ok(orderDTO);
    }
}
