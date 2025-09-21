package lk.ijse.backend.dto;

import lk.ijse.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDTO {
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String contactNumber;
    private Role role;
}
