package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuctionDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.BidDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.WatchlistDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Auction;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Bid;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Vehicle;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Watchlist;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.AuctionStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.BidStatus;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.AuctionRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.BidRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.VehicleRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.WatchlistRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.OrderRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AuctionExpirationService;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final WatchlistRepository watchlistRepository;
    private final OrderRepository orderRepository;
    private final AuctionExpirationService auctionExpirationService;
    private final VehicleServiceImpl vehicleService;

    @Override
    @Transactional
    public AuctionDTO saveAuction(AuctionDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new CustomeException(404, "Vehicle not found for auction"));

        User seller = null;
        if (dto.getSellerId() != null) {
            seller = userRepository.findById(dto.getSellerId()).orElse(null);
        }
        if (seller == null) {
            seller = vehicle.getUser();
        }
        if (seller == null) {
            List<User> users = userRepository.findAll();
            if (!users.isEmpty()) seller = users.get(0);
        }

        Auction auction = new Auction();
        auction.setVehicle(vehicle);
        auction.setSeller(seller);
        auction.setTitle(dto.getTitle() != null ? dto.getTitle() : vehicle.getTitle());
        auction.setAuctionType(dto.getType() != null ? dto.getType() : "VEHICLE");
        BigDecimal startP = dto.getStartingPrice() != null ? dto.getStartingPrice() : (dto.getStartPrice() != null ? dto.getStartPrice() : vehicle.getPrice());
        auction.setStartingPrice(startP != null ? startP : BigDecimal.ZERO);
        auction.setReservePrice(dto.getReservePrice());
        auction.setCurrentBid(startP != null ? startP : BigDecimal.ZERO);
        auction.setMinBidIncrement(dto.getMinBidIncrement() != null ? dto.getMinBidIncrement() : BigDecimal.valueOf(100.00));
        auction.setBuyItNowPrice(dto.getBuyItNowPrice());
        auction.setStartDate(dto.getStartDate() != null ? dto.getStartDate() : LocalDateTime.now());
        auction.setEndDate(dto.getEndDate() != null ? dto.getEndDate() : LocalDateTime.now().plusDays(7));
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setBidCount(0);
        auction.setBidderCount(0);
        auction.setViews(0);
        auction.setIsApproved(true);
        auction.setIsFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false);
        auction.setCreatedAt(LocalDateTime.now());
        auction.setUpdatedAt(LocalDateTime.now());

        Auction saved = auctionRepository.save(auction);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public AuctionDTO updateAuction(Long id, AuctionDTO dto) {
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Auction not found"));

        if (dto.getTitle() != null) auction.setTitle(dto.getTitle());
        if (dto.getReservePrice() != null) auction.setReservePrice(dto.getReservePrice());
        if (dto.getEndDate() != null) auction.setEndDate(dto.getEndDate());
        if (dto.getStatus() != null) auction.setStatus(dto.getStatus());
        if (dto.getIsFeatured() != null) auction.setIsFeatured(dto.getIsFeatured());
        auction.setUpdatedAt(LocalDateTime.now());

        return convertToDTO(auctionRepository.save(auction));
    }

    @Override
    @Transactional
    public void deleteAuction(Long id) {
        if (!auctionRepository.existsById(id)) {
            throw new CustomeException(404, "Auction not found");
        }
        auctionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public AuctionDTO getAuctionById(Long id) {
        auctionExpirationService.processAuctionIfExpired(id);
        Auction auction = auctionRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Auction not found"));
        return convertToDTO(auction);
    }

    @Override
    @Transactional
    public List<AuctionDTO> getAllAuctions() {
        auctionExpirationService.processExpiredAuctions();
        return auctionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AuctionDTO> getActiveAuctions() {
        auctionExpirationService.processExpiredAuctions();
        return auctionRepository.findByStatus(AuctionStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionDTO> getUpcomingAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.SCHEDULED).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionDTO> getEndedAuctions() {
        return auctionRepository.findByStatus(AuctionStatus.ENDED).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuctionDTO> getUserAuctions(Long userId) {
        return auctionRepository.findBySeller_UserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BidDTO placeBid(Long auctionId, Long userId, BigDecimal amount) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomeException(404, "Auction not found"));

        if (auction.getStatus() != AuctionStatus.ACTIVE) {
            throw new CustomeException(400, "Auction is not active");
        }

        if (auction.getEndDate() != null && !LocalDateTime.now().isBefore(auction.getEndDate())) {
            auctionExpirationService.processSingleAuctionExpiration(auction);
            throw new CustomeException(400, "Auction has ended and bidding is closed");
        }

        BigDecimal minRequired = auction.getCurrentBid().add(auction.getMinBidIncrement());
        if (amount.compareTo(minRequired) < 0) {
            throw new CustomeException(400, "Bid must be at least $" + minRequired);
        }

        User bidder = userRepository.findById(userId)
                .orElseThrow(() -> new CustomeException(404, "Bidder user not found"));

        // Mark previous bids as OUTBID
        List<Bid> previousBids = bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId);
        for (Bid prev : previousBids) {
            if (prev.getBidStatus() == BidStatus.WINNING || prev.getBidStatus() == BidStatus.ACTIVE) {
                prev.setBidStatus(BidStatus.OUTBID);
                bidRepository.save(prev);
            }
        }

        // Create new winning bid
        Bid newBid = new Bid();
        newBid.setAuction(auction);
        newBid.setUser(bidder);
        newBid.setBidAmount(amount);
        newBid.setBidStatus(BidStatus.WINNING);
        newBid.setPlacedAt(LocalDateTime.now());
        Bid savedBid = bidRepository.save(newBid);

        // Update auction
        auction.setCurrentBid(amount);
        auction.setHighestBidder(bidder);
        auction.setBidCount((auction.getBidCount() != null ? auction.getBidCount() : 0) + 1);

        // Calculate unique bidder count
        List<Bid> allBids = bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId);
        long uniqueBidders = allBids.stream().map(b -> b.getUser().getUserId()).distinct().count();
        auction.setBidderCount((int) uniqueBidders);
        auction.setUpdatedAt(LocalDateTime.now());
        auctionRepository.save(auction);

        return convertBidToDTO(savedBid);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidDTO> getBidsForAuction(Long auctionId) {
        return bidRepository.findByAuction_AuctionIdOrderByBidAmountDesc(auctionId).stream()
                .map(this::convertBidToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BidDTO> getRecentBidders(Long auctionId) {
        return bidRepository.findTop10ByAuction_AuctionIdOrderByPlacedAtDesc(auctionId).stream()
                .map(this::convertBidToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WatchlistDTO> getWatchlist(Long userId) {
        return watchlistRepository.findByUser_UserId(userId).stream()
                .map(w -> {
                    WatchlistDTO dto = new WatchlistDTO();
                    dto.setWatchlistId(w.getWatchlistId());
                    dto.setUserId(userId);
                    dto.setAuctionId(w.getAuction().getAuctionId());
                    dto.setAuction(convertToDTO(w.getAuction()));
                    dto.setCreatedAt(w.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WatchlistDTO addToWatchlist(Long userId, Long auctionId) {
        Optional<Watchlist> existing = watchlistRepository.findByUser_UserIdAndAuction_AuctionId(userId, auctionId);
        if (existing.isPresent()) {
            Watchlist w = existing.get();
            WatchlistDTO dto = new WatchlistDTO();
            dto.setWatchlistId(w.getWatchlistId());
            dto.setUserId(userId);
            dto.setAuctionId(auctionId);
            dto.setAuction(convertToDTO(w.getAuction()));
            dto.setCreatedAt(w.getCreatedAt());
            return dto;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomeException(404, "User not found"));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomeException(404, "Auction not found"));

        Watchlist w = new Watchlist();
        w.setUser(user);
        w.setAuction(auction);
        w.setCreatedAt(LocalDateTime.now());
        Watchlist saved = watchlistRepository.save(w);

        WatchlistDTO dto = new WatchlistDTO();
        dto.setWatchlistId(saved.getWatchlistId());
        dto.setUserId(userId);
        dto.setAuctionId(auctionId);
        dto.setAuction(convertToDTO(auction));
        dto.setCreatedAt(saved.getCreatedAt());
        return dto;
    }

    @Override
    @Transactional
    public void removeFromWatchlist(Long userId, Long auctionId) {
        watchlistRepository.deleteByUser_UserIdAndAuction_AuctionId(userId, auctionId);
    }

    @Override
    @Transactional
    public AuctionDTO approveAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomeException(404, "Auction not found"));
        auction.setIsApproved(true);
        auction.setStatus(AuctionStatus.ACTIVE);
        auction.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(auctionRepository.save(auction));
    }

    @Override
    @Transactional
    public AuctionDTO rejectAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new CustomeException(404, "Auction not found"));
        auction.setIsApproved(false);
        auction.setStatus(AuctionStatus.CANCELLED);
        auction.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(auctionRepository.save(auction));
    }

    public AuctionDTO convertToDTO(Auction a) {
        AuctionDTO dto = new AuctionDTO();
        dto.setId(a.getAuctionId());
        dto.setAuctionId(a.getAuctionId());
        dto.setServerTime(LocalDateTime.now().toString());
        dto.setTitle(a.getTitle());
        dto.setType(a.getAuctionType());
        dto.setStartingPrice(a.getStartingPrice());
        dto.setStartPrice(a.getStartingPrice());
        dto.setReservePrice(a.getReservePrice());
        dto.setCurrentBid(a.getCurrentBid());
        dto.setHighestBid(a.getCurrentBid());
        dto.setMinBidIncrement(a.getMinBidIncrement());
        dto.setBuyItNowPrice(a.getBuyItNowPrice());
        dto.setStartDate(a.getStartDate());
        dto.setEndDate(a.getEndDate());
        dto.setEndTime(a.getEndDate());
        dto.setExtendedEndDate(a.getExtendedEndDate());
        dto.setAutoExtendEnabled(a.getAutoExtendEnabled());
        dto.setAutoExtendMinutes(a.getAutoExtendMinutes());
        dto.setStatus(a.getStatus());
        dto.setBidCount(a.getBidCount());
        dto.setBidderCount(a.getBidderCount());
        dto.setViews(a.getViews());
        dto.setIsFeatured(a.getIsFeatured());
        dto.setIsApproved(a.getIsApproved());
        dto.setCreatedAt(a.getCreatedAt());
        dto.setUpdatedAt(a.getUpdatedAt());

        if (a.getEndDate() != null) {
            long seconds = Duration.between(LocalDateTime.now(), a.getEndDate()).getSeconds();
            dto.setTimeLeftSeconds(Math.max(0, seconds));
        }

        if (a.getSeller() != null) {
            dto.setSellerId(a.getSeller().getUserId());
            dto.setSellerName(a.getSeller().getUserName());
        }

        if (a.getHighestBidder() != null) {
            dto.setHighestBidderId(a.getHighestBidder().getUserId());
            dto.setHighestBidderName(a.getHighestBidder().getUserName());
        }

        if (a.getVehicle() != null) {
            dto.setVehicleId(a.getVehicle().getVehicleId());
            dto.setVehicle(vehicleService.convertToDTO(a.getVehicle()));
        }

        int watchCount = watchlistRepository.countByAuction_AuctionId(a.getAuctionId());
        dto.setWatchCount(watchCount);

        List<Bid> recent = bidRepository.findTop10ByAuction_AuctionIdOrderByPlacedAtDesc(a.getAuctionId());
        dto.setRecentBidders(recent.stream().map(this::convertBidToDTO).collect(Collectors.toList()));

        if (orderRepository != null) {
            orderRepository.findByAuction_AuctionId(a.getAuctionId()).ifPresent(order -> {
                dto.setOrderId(order.getOrderId());
                dto.setOrderNumber(order.getOrderNumber());
                dto.setOrderCreated(true);
            });
        }

        return dto;
    }

    private BidDTO convertBidToDTO(Bid b) {
        BidDTO dto = new BidDTO();
        dto.setId(b.getBidId());
        dto.setAuctionId(b.getAuction().getAuctionId());
        if (b.getUser() != null) {
            dto.setUserId(b.getUser().getUserId());
            dto.setUserName(b.getUser().getUserName());
            String name = b.getUser().getUserName();
            if (name != null && name.length() > 2) {
                dto.setMaskedBidderName(name.charAt(0) + "***" + name.charAt(name.length() - 1));
            } else {
                dto.setMaskedBidderName("b***r");
            }
        }
        dto.setAmount(b.getBidAmount());
        dto.setStatus(b.getBidStatus());
        dto.setTimestamp(b.getPlacedAt());
        return dto;
    }
}
