package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.BidStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidDTO {
    private Long id;
    private Long auctionId;
    private Long userId;
    private String userName;
    private String maskedBidderName;
    private BigDecimal amount;
    private BidStatus status;
    private LocalDateTime timestamp;

    public BigDecimal getBidAmount() {
        return amount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.amount = bidAmount;
    }
}
