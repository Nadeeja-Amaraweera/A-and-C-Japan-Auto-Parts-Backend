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
    private String password;
    @Enumerated(EnumType.STRING)
    private Role userRole;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    public User(String userStringId, String userName, String password, Role userrole, UserStatus userStatus) {
        this.userStringId = userStringId;
        this.userName = userName;
        this.password = password;
        this.userRole = userrole;
        this.userStatus = userStatus;
    }
}