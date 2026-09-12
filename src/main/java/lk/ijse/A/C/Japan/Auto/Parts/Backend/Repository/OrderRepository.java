package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Order;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByOrderStatus(OrderStatus orderStatus);
    Optional<Order> findByAuction_AuctionId(Long auctionId);
    boolean existsByAuction_AuctionId(Long auctionId);
}
