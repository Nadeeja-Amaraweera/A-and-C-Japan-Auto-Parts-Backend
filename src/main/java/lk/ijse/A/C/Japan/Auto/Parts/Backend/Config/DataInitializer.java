package lk.ijse.A.C.Japan.Auto.Parts.Backend.Config;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.Role;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.UserStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User admin = userRepository.findByUserEmail("admin@acjapan.com").orElse(null);

        if (admin == null) {
            admin = new User();
            admin.setUserStringId("U001");
            admin.setUserName("System Admin");
            admin.setUserEmail("admin@acjapan.com");
            admin.setUserPassword(passwordEncoder.encode("admin123"));
            admin.setUserRole(Role.ADMIN);
            admin.setUserStatus(UserStatus.ACTIVE);
            admin.setUserPhone("+81 90 1234 5678");
            admin.setUserAddress("Tokyo, Japan");
            userRepository.save(admin);
            System.out.println("✅ Seeded Admin: admin@acjapan.com / admin123");
        }
    }
}
