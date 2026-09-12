package lk.ijse.A.C.Japan.Auto.Parts.Backend.Config;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AuctionExpirationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionExpirationScheduler {

    private final AuctionExpirationService auctionExpirationService;

    /**
     * Periodically check for expired active auctions and process winner / order creation.
     * Runs every 5 seconds.
     */
    @Scheduled(fixedDelay = 5000)
    public void runAuctionExpirationCheck() {
        try {
            auctionExpirationService.processExpiredAuctions();
        } catch (Exception e) {
            log.error("Error running auction expiration scheduler: {}", e.getMessage(), e);
        }
    }
}
