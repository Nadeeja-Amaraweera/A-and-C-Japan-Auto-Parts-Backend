package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDTO {
    private Long id;
    private Long auctionId; // compatibility alias for frontend
    private String serverTime;
    private String title;
    private Long vehicleId;
    private VehicleDTO vehicle;
    private String type;
    private BigDecimal startingPrice;
    private BigDecimal startPrice; // compatibility with frontend templates
    private BigDecimal reservePrice;
    private BigDecimal currentBid;
    private BigDecimal highestBid; // compatibility with frontend templates
    private BigDecimal minBidIncrement;
    private BigDecimal buyItNowPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime endTime; // compatibility with frontend templates
    private LocalDateTime extendedEndDate;
    private Boolean autoExtendEnabled;
    private Integer autoExtendMinutes;
    private AuctionStatus status;
    private Integer bidCount;
    private Integer bidderCount;
    private Integer views;
    private Integer watchCount;
    private Boolean isFeatured;
    private Long sellerId;
    private String sellerName;
    private Long highestBidderId;
    private String highestBidderName;
    private Boolean isApproved;
    private Long timeLeftSeconds;
    private Long orderId;
    private String orderNumber;
    private Boolean orderCreated;
    private List<BidDTO> bids = new ArrayList<>();
    private List<BidDTO> recentBidders = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
