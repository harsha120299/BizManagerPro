// UserBusinessDTO.java
package lk.ijse.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBusinessDTO {
    private Long id;
    private String role;
    private BusinessDTO business;
}
