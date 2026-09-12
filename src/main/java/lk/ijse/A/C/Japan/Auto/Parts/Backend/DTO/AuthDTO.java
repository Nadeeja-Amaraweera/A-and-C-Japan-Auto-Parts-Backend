package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    private String userEmail;
    private String email;
    private String password;

    public String getUserEmail() {
        return userEmail != null ? userEmail : email;
    }
}
