package lk.ijse.backend.dto;

import lk.ijse.backend.entity.Role;
import lk.ijse.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private User user;
    private String role;
    private String refreshToken;

}
