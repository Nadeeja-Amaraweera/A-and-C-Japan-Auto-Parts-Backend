package lk.ijse.A.C.Japan.Auto.Parts.Backend.Security;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User> optionalUser = userRepository.findByUserName(username);

        if(optionalUser.isEmpty())
            throw new RuntimeException("Sorry no user");


        return User.builder()
                .username(optionalUser.get().getUserName())
                .password(optionalUser.get().getUserPassword())
                .roles(optionalUser.get().getUserRole().name())
                .build();
    }

}
