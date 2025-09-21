package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private Long itemId;

    private double quantity;
    private Double price;

    private Long orderId;    // Reference to Order
    private Long productId;  // Reference to Product
}
