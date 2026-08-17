package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.Role;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.UserStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Saving user: {}", userDTO);

        if (userDTO.getUserRole().equals("")) {
            throw new CustomeException(404, "Cannot create user with ADMIN role");
        }

        User user = new User();
        user.setUserStringId(generateUserId());
        user.setUserName(userDTO.getUserName());
        user.setUserPassword(userDTO.getUserPassword());
        user.setUserRole(userDTO.getUserRole());
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserStatus(UserStatus.ACTIVE);

        User saveUser = userRepository.save(user);
        log.info("User saved successfully: {}", saveUser);
        return new UserDTO(saveUser.getUserStringId(), saveUser.getUserName(), saveUser.getUserEmail(), saveUser.getUserPassword(), saveUser.getUserRole(), saveUser.getUserStatus());
    }

    public String generateUserId() {
        log.info("Generating user ID");
        String lastUserId = userRepository.findLastUserId();
        String newUserId;

        if (lastUserId == null) {
            newUserId = "U001";
        } else {
            int lastId = Integer.parseInt(lastUserId.substring(1));
            int newId = lastId + 1;
            newUserId = String.format("U%03d", newId);
        }
        return newUserId;
    }
}
