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
    private String userPhone;
    private String userAddress;
    private Role userRole;
    private UserStatus userStatus;
    private boolean supplierApprovalStatus;

    public UserDTO(String userStringId, String userName, String userEmail, String userPassword, String userPhone,String userAddress, Role userRole, UserStatus userStatus) {
        this.userStringId = userStringId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    public UserDTO(String userStringId, String userName, String userEmail, String userPassword, String userPhone, String userAddress, Role userRole, UserStatus userStatus, boolean supplierApprovalStatus) {
        this.userStringId = userStringId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userAddress = userAddress;
        this.userRole = userRole;
        this.userPhone = userPhone;
        this.userStatus = userStatus;
        this.supplierApprovalStatus = supplierApprovalStatus;
    }

    public UserDTO(Long userId, String userStringId, String userName, String userEmail, String userPassword, String userPhone, String userAddress, Role userRole, UserStatus userStatus) {
        this.userId = userId;
        this.userStringId = userStringId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }
}
