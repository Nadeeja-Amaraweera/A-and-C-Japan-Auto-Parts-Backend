package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.Role;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String userStringId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private Role userRole;
    private UserStatus userStatus;

    public UserDTO(String userStringId, String userName, String userEmail, String userPassword, Role userRole, UserStatus userStatus) {
        this.userStringId = userStringId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }
}
