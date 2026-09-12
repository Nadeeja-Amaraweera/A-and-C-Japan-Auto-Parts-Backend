package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistDTO {
    private Long watchlistId;
    private Long userId;
    private Long auctionId;
    private AuctionDTO auction;
    private LocalDateTime createdAt;
}
