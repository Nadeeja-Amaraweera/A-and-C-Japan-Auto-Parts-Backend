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
    private String userPhone;
    private String userAddress;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
}