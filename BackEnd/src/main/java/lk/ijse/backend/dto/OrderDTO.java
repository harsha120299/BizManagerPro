package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long orderId;

    private LocalDate orderDate;
    private String status;
    private Double totalAmount;
    private String note;

    private Long customerId;                  // Reference to Customer
    private List<OrderItemDTO> orderItems;    // List of order items DTOs
    private Long incomeId;
    private Long businessId;// Reference to associated Income, if any
}
