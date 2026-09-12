package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuctionDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.BidDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.WatchlistDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AuctionService {
    AuctionDTO saveAuction(AuctionDTO dto);
    AuctionDTO updateAuction(Long id, AuctionDTO dto);
    void deleteAuction(Long id);
    AuctionDTO getAuctionById(Long id);
    List<AuctionDTO> getAllAuctions();
    List<AuctionDTO> getActiveAuctions();
    List<AuctionDTO> getUpcomingAuctions();
    List<AuctionDTO> getEndedAuctions();
    List<AuctionDTO> getUserAuctions(Long userId);
    BidDTO placeBid(Long auctionId, Long userId, BigDecimal amount);
    List<BidDTO> getBidsForAuction(Long auctionId);
    List<BidDTO> getRecentBidders(Long auctionId);
    List<WatchlistDTO> getWatchlist(Long userId);
    WatchlistDTO addToWatchlist(Long userId, Long auctionId);
    void removeFromWatchlist(Long userId, Long auctionId);
    AuctionDTO approveAuction(Long auctionId);
    AuctionDTO rejectAuction(Long auctionId);
}
