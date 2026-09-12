package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser_UserId(Long userId);
    Optional<Watchlist> findByUser_UserIdAndAuction_AuctionId(Long userId, Long auctionId);
    void deleteByUser_UserIdAndAuction_AuctionId(Long userId, Long auctionId);
    int countByAuction_AuctionId(Long auctionId);
}
