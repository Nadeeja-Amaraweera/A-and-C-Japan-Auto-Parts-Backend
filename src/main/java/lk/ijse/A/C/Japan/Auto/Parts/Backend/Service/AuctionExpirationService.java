package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Auction;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Bid;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.AuctionStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.BidStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.AuctionRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.BidRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionExpirationService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Transactional
    public void processExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> expiredAuctions = auctionRepository.findByStatusAndEndDateLessThanEqual(AuctionStatus.ACTIVE, now);

        if (expiredAuctions.isEmpty()) {
            return;
        }

        System.out.println("⏱️ Found " + expiredAuctions.size() + " expired active auction(s) to process at " + now);

        for (Auction auction : expiredAuctions) {
            try {
                processSingleAuctionExpiration(auction);
            } catch (Exception e) {
                System.err.println("❌ Error processing expired auction #" + auction.getAuctionId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void processAuctionIfExpired(Long auctionId) {
        if (auctionId == null) return;
        auctionRepository.findById(auctionId).ifPresent(auction -> {
            if (auction.getStatus() == AuctionStatus.ACTIVE && !LocalDateTime.now().isBefore(auction.getEndDate())) {
                processSingleAuctionExpiration(auction);
            }
        });
    }

    @Transactional
    public void processSingleAuctionExpiration(Auction auction) {
        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            return;
        }

        // 1. Fetch valid bids ordered by bidAmount DESC, then earliest placedAt ASC (tie-breaker)
        List<Bid> bids = bidRepository.findByAuction_AuctionIdOrderByBidAmountDescPlacedAtAsc(auction.getAuctionId());

        if (bids.isEmpty()) {
            // No bids case
            auction.setStatus(AuctionStatus.ENDED);
            auction.setUpdatedAt(LocalDateTime.now());
            auctionRepository.save(auction);
            System.out.println("ℹ️ Auction #" + auction.getAuctionId() + " (" + auction.getTitle() + ") ended with NO bids. Marked ENDED, 0 orders created.");
            return;
        }

        // 2. Highest bidder & amount
        Bid winningBid = bids.get(0);
        User winner = winningBid.getUser();
        BigDecimal winningAmount = winningBid.getBidAmount();

        // 3. Mark winning bid and outbid others
        winningBid.setBidStatus(BidStatus.WINNING);
        bidRepository.save(winningBid);

        for (int i = 1; i < bids.size(); i++) {
            Bid other = bids.get(i);
            if (other.getBidStatus() != BidStatus.OUTBID) {
                other.setBidStatus(BidStatus.OUTBID);
                bidRepository.save(other);
            }
        }

        // 4. Update auction record
        auction.setStatus(AuctionStatus.ENDED);
        auction.setHighestBidder(winner);
        auction.setCurrentBid(winningAmount);
        auction.setUpdatedAt(LocalDateTime.now());
        Auction savedAuction = auctionRepository.save(auction);

        // 5. Create Order for winning bidder with strict duplicate check
        if (!orderRepository.existsByAuction_AuctionId(savedAuction.getAuctionId())) {
            orderService.createOrderForWinningBidder(savedAuction, winner, winningAmount);
        } else {
            System.out.println("ℹ️ Order already exists for auction #" + savedAuction.getAuctionId() + ". Duplicate prevented.");
        }
    }
}
