package lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Auction;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {
    List<Auction> findByStatus(AuctionStatus status);
    List<Auction> findByStatusAndEndDateLessThanEqual(AuctionStatus status, java.time.LocalDateTime now);
    List<Auction> findBySeller_UserId(Long sellerId);
    List<Auction> findByIsApprovedFalse();
    Optional<Auction> findByVehicle_VehicleId(Long vehicleId);
    List<Auction> findByIsFeaturedTrueAndStatus(AuctionStatus status);
}
