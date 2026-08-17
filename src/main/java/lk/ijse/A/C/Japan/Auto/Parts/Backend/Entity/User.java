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
    private Long userId;
    private String userStringId;
    private String userName;
    private String userPassword;
    @Enumerated(EnumType.STRING)
    private Role userRole;
    private String userEmail;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public User(String userStringId, String userName, String userPassword, Role userRole, String userEmail, UserStatus userStatus) {
        this.userStringId = userStringId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
    }
}