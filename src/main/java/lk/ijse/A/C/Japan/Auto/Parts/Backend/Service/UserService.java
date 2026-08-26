package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDTO;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    UserDTO getUserDetails(String email, String password);

}
