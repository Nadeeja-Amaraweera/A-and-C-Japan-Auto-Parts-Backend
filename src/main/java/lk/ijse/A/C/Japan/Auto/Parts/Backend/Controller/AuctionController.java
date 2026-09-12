package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuctionDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.BidDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.WatchlistDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/auctions")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "*"})
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping(value = {"", "/active"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getActiveAuctions() {
        List<AuctionDTO> auctions = auctionService.getActiveAuctions();
        return new CommonResponse(OPERATION_SUCCESS, auctions, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllAuctions() {
        List<AuctionDTO> auctions = auctionService.getAllAuctions();
        return new CommonResponse(OPERATION_SUCCESS, auctions, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/featured", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getFeaturedAuctions() {
        List<AuctionDTO> auctions = auctionService.getActiveAuctions().stream()
                .filter(a -> Boolean.TRUE.equals(a.getIsFeatured()))
                .collect(Collectors.toList());
        return new CommonResponse(OPERATION_SUCCESS, auctions, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAuctionById(@PathVariable Long id) {
        AuctionDTO auction = auctionService.getAuctionById(id);
        return new CommonResponse(OPERATION_SUCCESS, auction, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserAuctions(@PathVariable Long userId) {
        List<AuctionDTO> auctions = auctionService.getUserAuctions(userId);
        return new CommonResponse(OPERATION_SUCCESS, auctions, SUCCESS_MESSAGE);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse createAuction(@RequestBody AuctionDTO auctionDTO) {
        AuctionDTO created = auctionService.saveAuction(auctionDTO);
        return new CommonResponse(OPERATION_SUCCESS, created, SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateAuction(@PathVariable Long id, @RequestBody AuctionDTO auctionDTO) {
        AuctionDTO updated = auctionService.updateAuction(id, auctionDTO);
        return new CommonResponse(OPERATION_SUCCESS, updated, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteAuction(@PathVariable Long id) {
        auctionService.deleteAuction(id);
        return new CommonResponse(OPERATION_SUCCESS, null, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/{id}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse placeBid(@PathVariable Long id, @RequestBody BidDTO bidDTO) {
        BidDTO placedBid = auctionService.placeBid(id, bidDTO.getUserId(), bidDTO.getBidAmount());
        return new CommonResponse(OPERATION_SUCCESS, placedBid, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}/bids", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAuctionBids(@PathVariable Long id) {
        List<BidDTO> bids = auctionService.getBidsForAuction(id);
        return new CommonResponse(OPERATION_SUCCESS, bids, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}/recent-bidders", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getRecentBidders(@PathVariable Long id) {
        List<BidDTO> bidders = auctionService.getRecentBidders(id);
        return new CommonResponse(OPERATION_SUCCESS, bidders, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/{id}/watchlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse addToWatchlist(@PathVariable Long id, @RequestParam Long userId) {
        WatchlistDTO watchlist = auctionService.addToWatchlist(userId, id);
        return new CommonResponse(OPERATION_SUCCESS, watchlist, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{id}/watchlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse removeFromWatchlist(@PathVariable Long id, @RequestParam Long userId) {
        auctionService.removeFromWatchlist(userId, id);
        return new CommonResponse(OPERATION_SUCCESS, null, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/watchlist/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserWatchlist(@PathVariable Long userId) {
        List<WatchlistDTO> watchlist = auctionService.getWatchlist(userId);
        return new CommonResponse(OPERATION_SUCCESS, watchlist, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/{id}/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse approveAuction(@PathVariable Long id) {
        AuctionDTO approved = auctionService.approveAuction(id);
        return new CommonResponse(OPERATION_SUCCESS, approved, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/{id}/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse rejectAuction(@PathVariable Long id) {
        AuctionDTO rejected = auctionService.rejectAuction(id);
        return new CommonResponse(OPERATION_SUCCESS, rejected, SUCCESS_MESSAGE);
    }
}
