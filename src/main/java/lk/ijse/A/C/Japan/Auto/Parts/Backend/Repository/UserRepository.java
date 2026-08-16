package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT user_string_id FROM user ORDER BY user_id DESC LIMIT 1", nativeQuery = true)
    String findLastUserId();
}
