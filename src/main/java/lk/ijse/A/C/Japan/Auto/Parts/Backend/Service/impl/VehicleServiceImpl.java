package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.impl;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.VehicleDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.VehicleImageDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Category;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.User;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.Vehicle;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Entity.VehicleImage;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.ConditionType;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.FuelType;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.TransmissionType;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Exception.CustomeException;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.CategoryRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.UserRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.VehicleImageRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Repository.VehicleRepository;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleImageRepository vehicleImageRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public VehicleDTO saveVehicle(VehicleDTO dto) {
        User user = null;
        if (dto.getUserId() != null) {
            user = userRepository.findById(dto.getUserId()).orElse(null);
        }
        if (user == null) {
            // fallback to first available user or create default
            List<User> users = userRepository.findAll();
            if (!users.isEmpty()) {
                user = users.get(0);
            } else {
                throw new CustomeException(400, "Valid seller user ID required");
            }
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setUser(user);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            vehicle.setCategory(category);
        }

        vehicle.setTitle(dto.getTitle() != null ? dto.getTitle() : "Vehicle Listing");
        vehicle.setBrand(dto.getBrand() != null ? dto.getBrand() : "Unknown");
        vehicle.setModel(dto.getModel() != null ? dto.getModel() : "Model");
        vehicle.setYear(dto.getYear() != null ? dto.getYear() : 2020);
        vehicle.setMileage(dto.getMileage() != null ? dto.getMileage() : 0);
        vehicle.setPrice(dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO);
        vehicle.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "USD");
        vehicle.setVehicleCondition(dto.getCondition() != null ? dto.getCondition() : ConditionType.USED);
        vehicle.setTransmission(dto.getTransmission() != null ? dto.getTransmission() : TransmissionType.AUTOMATIC);
        vehicle.setFuelType(dto.getFuelType() != null ? dto.getFuelType() : FuelType.GASOLINE);
        vehicle.setEngineSize(dto.getEngineSize());
        vehicle.setEnginePower(dto.getEnginePower());
        vehicle.setExteriorColor(dto.getColor());
        vehicle.setInteriorColor(dto.getInteriorColor());
        vehicle.setDoors(dto.getDoors() != null ? dto.getDoors() : 4);
        vehicle.setSeats(dto.getSeats() != null ? dto.getSeats() : 5);
        vehicle.setDrivetrain(dto.getDrivetrain() != null ? dto.getDrivetrain() : "FWD");
        vehicle.setVin(dto.getVin());
        vehicle.setLicensePlate(dto.getLicensePlate());
        vehicle.setLocationAddress(dto.getLocationAddress());
        vehicle.setLocationCity(dto.getLocationCity());
        vehicle.setLocationCountry(dto.getLocationCountry() != null ? dto.getLocationCountry() : "Japan");
        vehicle.setDescription(dto.getDescription());
        vehicle.setStatus("APPROVED");
        vehicle.setInspectionDate(dto.getInspectionDate());
        vehicle.setInspectionResult(dto.getInspectionResult());
        vehicle.setViews(0);
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());

        Vehicle saved = vehicleRepository.save(vehicle);

        // Add primary or initial images if provided
        List<String> imgList = (dto.getImages() != null && !dto.getImages().isEmpty())
                ? dto.getImages()
                : dto.getImageUrls();
        if (imgList != null && !imgList.isEmpty()) {
            boolean isFirst = true;
            for (String imgUrl : imgList) {
                VehicleImage img = new VehicleImage();
                img.setVehicle(saved);
                img.setImageUrl(imgUrl);
                img.setIsPrimary(isFirst);
                img.setDisplayOrder(isFirst ? 0 : 1);
                vehicleImageRepository.save(img);
                saved.getImages().add(img);
                isFirst = false;
            }
        } else if (dto.getPrimaryImage() != null && !dto.getPrimaryImage().isEmpty()) {
            VehicleImage img = new VehicleImage();
            img.setVehicle(saved);
            img.setImageUrl(dto.getPrimaryImage());
            img.setIsPrimary(true);
            img.setDisplayOrder(0);
            vehicleImageRepository.save(img);
            saved.getImages().add(img);
        }

        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Vehicle not found"));

        if (dto.getTitle() != null) vehicle.setTitle(dto.getTitle());
        if (dto.getBrand() != null) vehicle.setBrand(dto.getBrand());
        if (dto.getModel() != null) vehicle.setModel(dto.getModel());
        if (dto.getYear() != null) vehicle.setYear(dto.getYear());
        if (dto.getMileage() != null) vehicle.setMileage(dto.getMileage());
        if (dto.getPrice() != null) vehicle.setPrice(dto.getPrice());
        if (dto.getCondition() != null) vehicle.setVehicleCondition(dto.getCondition());
        if (dto.getTransmission() != null) vehicle.setTransmission(dto.getTransmission());
        if (dto.getFuelType() != null) vehicle.setFuelType(dto.getFuelType());
        if (dto.getEngineSize() != null) vehicle.setEngineSize(dto.getEngineSize());
        if (dto.getColor() != null) vehicle.setExteriorColor(dto.getColor());
        if (dto.getDescription() != null) vehicle.setDescription(dto.getDescription());
        vehicle.setUpdatedAt(LocalDateTime.now());

        return convertToDTO(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new CustomeException(404, "Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new CustomeException(404, "Vehicle not found"));
        return convertToDTO(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUser_UserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> filterVehicles(String brand, String condition, String transmission, BigDecimal minPrice, BigDecimal maxPrice, Integer minYear, Integer maxYear) {
        return vehicleRepository.findAll().stream()
                .filter(v -> brand == null || brand.isEmpty() || "All Brands".equalsIgnoreCase(brand) || v.getBrand().equalsIgnoreCase(brand))
                .filter(v -> condition == null || condition.isEmpty() || v.getVehicleCondition().name().equalsIgnoreCase(condition))
                .filter(v -> transmission == null || transmission.isEmpty() || v.getTransmission().name().equalsIgnoreCase(transmission))
                .filter(v -> minPrice == null || v.getPrice().compareTo(minPrice) >= 0)
                .filter(v -> maxPrice == null || v.getPrice().compareTo(maxPrice) <= 0)
                .filter(v -> minYear == null || v.getYear() >= minYear)
                .filter(v -> maxYear == null || v.getYear() <= maxYear)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleDTO> searchVehicles(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllVehicles();
        }
        return vehicleRepository.findByTitleContainingIgnoreCaseOrModelContainingIgnoreCase(query, query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VehicleDTO addImageToVehicle(Long vehicleId, String imageUrl, Boolean isPrimary) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new CustomeException(404, "Vehicle not found"));

        VehicleImage img = new VehicleImage();
        img.setVehicle(vehicle);
        img.setImageUrl(imageUrl);
        img.setIsPrimary(isPrimary != null ? isPrimary : false);
        vehicleImageRepository.save(img);

        return convertToDTO(vehicle);
    }

    public VehicleDTO convertToDTO(Vehicle v) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(v.getVehicleId());
        if (v.getUser() != null) {
            dto.setUserId(v.getUser().getUserId());
            dto.setSellerName(v.getUser().getUserName());
        }
        if (v.getCategory() != null) {
            dto.setCategoryId(v.getCategory().getCategoryId());
            dto.setCategoryName(v.getCategory().getCategoryName());
        }
        dto.setTitle(v.getTitle());
        dto.setBrand(v.getBrand());
        dto.setMake(v.getBrand());
        dto.setModel(v.getModel());
        dto.setYear(v.getYear());
        dto.setMileage(v.getMileage());
        dto.setPrice(v.getPrice());
        dto.setCurrency(v.getCurrency());
        dto.setCondition(v.getVehicleCondition());
        dto.setTransmission(v.getTransmission());
        dto.setFuelType(v.getFuelType());
        dto.setEngineSize(v.getEngineSize());
        dto.setEnginePower(v.getEnginePower());
        dto.setColor(v.getExteriorColor());
        dto.setInteriorColor(v.getInteriorColor());
        dto.setDoors(v.getDoors());
        dto.setSeats(v.getSeats());
        dto.setDrivetrain(v.getDrivetrain());
        dto.setVin(v.getVin());
        dto.setLicensePlate(v.getLicensePlate());
        dto.setLocationAddress(v.getLocationAddress());
        dto.setLocationCity(v.getLocationCity());
        dto.setLocationCountry(v.getLocationCountry());
        dto.setDescription(v.getDescription());
        dto.setStatus(v.getStatus());
        dto.setInspectionDate(v.getInspectionDate());
        dto.setInspectionResult(v.getInspectionResult());
        dto.setViews(v.getViews());
        dto.setCreatedAt(v.getCreatedAt());
        dto.setUpdatedAt(v.getUpdatedAt());

        List<VehicleImage> imgs = vehicleImageRepository.findByVehicleOrderByDisplayOrderAsc(v);
        List<String> imgUrls = new ArrayList<>();
        List<VehicleImageDTO> imgDetails = new ArrayList<>();
        String primary = null;

        for (VehicleImage img : imgs) {
            imgUrls.add(img.getImageUrl());
            imgDetails.add(new VehicleImageDTO(img.getImageId(), img.getImageUrl(), img.getIsPrimary(), img.getDisplayOrder()));
            if (Boolean.TRUE.equals(img.getIsPrimary()) && primary == null) {
                primary = img.getImageUrl();
            }
        }
        if (primary == null && !imgUrls.isEmpty()) {
            primary = imgUrls.get(0);
        }
        if (primary == null) {
            primary = "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?q=80&w=2070&auto=format&fit=crop";
        }

        dto.setPrimaryImage(primary);
        dto.setImages(imgUrls);
        dto.setImageUrls(imgUrls);
        dto.setImageDetails(imgDetails);

        return dto;
    }
}
