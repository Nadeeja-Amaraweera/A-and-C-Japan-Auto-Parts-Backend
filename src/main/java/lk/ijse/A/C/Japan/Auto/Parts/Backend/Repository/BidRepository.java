package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuction_AuctionIdOrderByBidAmountDesc(Long auctionId);
    List<Bid> findByAuction_AuctionIdOrderByBidAmountDescPlacedAtAsc(Long auctionId);
    List<Bid> findByUser_UserIdOrderByPlacedAtDesc(Long userId);
    List<Bid> findTop10ByAuction_AuctionIdOrderByPlacedAtDesc(Long auctionId);
}
