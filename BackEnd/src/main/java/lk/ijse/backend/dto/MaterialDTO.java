package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDTO {
    private Long materialId;
    private String name;
    private Double quantity;
    private String Type;
    private Double price;
    private String supplier;
    private Long businessId;
}

