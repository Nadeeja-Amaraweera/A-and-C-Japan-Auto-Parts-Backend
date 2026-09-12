package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonAlias({"name", "username"})
    private String userName;

    @JsonAlias({"email"})
    private String userEmail;

    @JsonAlias({"password"})
    private String userPassword;

    @JsonAlias({"phone", "phoneNumber"})
    private String userPhone;

    @JsonAlias({"address", "shippingAddress"})
    private String userAddress;

    @JsonAlias({"role"})
    private Role userRole;

    private UserStatus userStatus;
    private SupplierDTO supplier;

    public UserDTO(String userStringId, String userName, String userEmail, String userPassword, String userPhone, String userAddress, Role userRole, UserStatus userStatus) {
        this.userStringId = userStringId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userRole = userRole;
        this.userStatus = userStatus;
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

    // Helper aliases
    @JsonProperty("name")
    public String getName() {
        return userName;
    }

    public void setName(String n) {
        if (this.userName == null || this.userName.isEmpty()) {
            this.userName = n;
        }
    }

    @JsonProperty("email")
    public String getEmail() {
        return userEmail;
    }

    public void setEmail(String e) {
        if (this.userEmail == null || this.userEmail.isEmpty()) {
            this.userEmail = e;
        }
    }

    @JsonProperty("phone")
    public String getPhone() {
        return userPhone;
    }

    public void setPhone(String p) {
        if (this.userPhone == null || this.userPhone.isEmpty()) {
            this.userPhone = p;
        }
    }

    @JsonProperty("address")
    public String getAddress() {
        return userAddress;
    }

    public void setAddress(String a) {
        if (this.userAddress == null || this.userAddress.isEmpty()) {
            this.userAddress = a;
        }
    }

    @JsonProperty("role")
    public Role getRole() {
        return userRole;
    }

    public void setRole(Role r) {
        if (this.userRole == null) {
            this.userRole = r;
        }
    }
}
