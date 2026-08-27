package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT user_string_id FROM user ORDER BY user_id DESC LIMIT 1", nativeQuery = true)
    String findLastUserId();

    Optional<User> findByUserName(String username);

    Optional<User> findByUserEmailAndUserPassword(String email, String password);

    @Query("SELECT COUNT(u) FROM User u WHERE u.userEmail = :email")
    int countByUserEmail(@Param("email") String email);
}
