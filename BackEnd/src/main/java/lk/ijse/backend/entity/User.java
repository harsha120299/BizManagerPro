package lk.ijse.backend.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId"
)
@ToString(exclude = "userBusinesses")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;
    private String username;
    private String password;
    private String email;
    private Role role;
    private String contactNumber;
    private LocalDate lastLogin;

    @PrePersist
    public void setDefaultRole() {
        if (this.role == null) {
            this.role = Role.USER;
        }
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBusiness> userBusinesses;

}
