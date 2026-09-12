package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_UserId(Long userId);
}
