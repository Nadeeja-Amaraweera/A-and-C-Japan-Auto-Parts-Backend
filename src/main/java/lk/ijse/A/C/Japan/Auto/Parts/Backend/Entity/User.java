package lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity;

import jakarta.persistence.*;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.Role;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String userName;
    private String password;
    private Role role;
    private UserStatus userStatus;

    @PrePersist
    public void generateCustomerId() {
        this.userId = "USR" + String.format("%04d", this.id);
    }

}
