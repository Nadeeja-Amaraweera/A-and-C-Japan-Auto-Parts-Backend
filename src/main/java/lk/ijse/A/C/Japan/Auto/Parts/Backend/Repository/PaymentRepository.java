package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByUser_UserId(Long userId);
    Optional<Payment> findByOrder_OrderId(Long orderId);
}
