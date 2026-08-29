package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.Role;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.UserStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Saving user: {}", userDTO);

        if (userDTO.getUserRole() == null) {
            System.out.println("UserRole is null! Setting default role: USER");
            userDTO.setUserRole(Role.CUSTOMER);  // Default role
        }

        if (userDTO.getUserRole().equals(Role.ADMIN)) {
            throw new CustomeException(404, "Cannot create user with ADMIN role");
        }

        if (isEmailExists(userDTO.getUserEmail())) {
            throw new CustomeException(409, "User with email already exists");
        }

        User user = new User();
//        user.setUserId(userDTO.getUserId());
        user.setUserStringId(generateUserId());
        user.setUserName(userDTO.getUserName());
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        user.setUserRole(Role.CUSTOMER);
        user.setUserEmail(userDTO.getUserEmail());
        user.setUserPhone(userDTO.getUserPhone());
        user.setUserAddress(userDTO.getUserAddress());
        user.setUserStatus(UserStatus.ACTIVE);

        User saveUser = userRepository.save(user);
        log.info("User saved successfully: {}", saveUser);
        return new UserDTO( saveUser.getUserStringId(), saveUser.getUserName(), saveUser.getUserEmail(), null, saveUser.getUserPhone(), saveUser.getUserAddress(), saveUser.getUserRole(), saveUser.getUserStatus());
    }

    @Override
    public UserDTO getUserDetails(String email, String password) {
        Optional<User> optionalUser = userRepository.findByUserEmail(email);

        if (optionalUser.isEmpty()) {
            throw new CustomeException(403, "Invalid email or password");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new CustomeException(403, "Invalid email or password");
        }

        return new UserDTO(
                user.getUserId(),
                user.getUserStringId(),
                user.getUserName(),
                user.getUserEmail(),
                null,
                user.getUserPhone(),
                user.getUserAddress(),
                user.getUserRole(),
                user.getUserStatus()
        );
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

    public boolean isEmailExists(String email) {
        return userRepository.countByUserEmail(email) > 0;
    }
}
