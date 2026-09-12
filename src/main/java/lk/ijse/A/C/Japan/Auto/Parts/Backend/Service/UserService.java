package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDTO;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);

    void deleteUser(Long userId);

    UserDTO getUserById(Long userId);

    UserDTO getUserDetails(String email, String password);

    UserDTO getUserByUsername(String username);
    UserDTO getUserByEmail(String email);
}
