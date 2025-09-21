package lk.ijse.backend.dto;

import lk.ijse.backend.entity.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDTO {
    private Long businessId;
    private String name;
    private String type;
    private String address;
    private LocalDate createdDate;
    private CurrencyType currencyType;
    private boolean isEmployeeExist;
}
