package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductMaterialDTO {
    private Long id;
    private Double quantityUsed;
    private Long productId;
    private Long materialId;
}
