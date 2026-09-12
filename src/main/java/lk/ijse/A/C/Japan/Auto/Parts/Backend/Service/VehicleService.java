package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.VehicleDTO;

import java.math.BigDecimal;
import java.util.List;

public interface VehicleService {
    VehicleDTO saveVehicle(VehicleDTO dto);
    VehicleDTO updateVehicle(Long id, VehicleDTO dto);
    void deleteVehicle(Long id);
    VehicleDTO getVehicleById(Long id);
    List<VehicleDTO> getAllVehicles();
    List<VehicleDTO> getVehiclesByUser(Long userId);
    List<VehicleDTO> filterVehicles(String brand, String condition, String transmission, BigDecimal minPrice, BigDecimal maxPrice, Integer minYear, Integer maxYear);
    List<VehicleDTO> searchVehicles(String query);
    VehicleDTO addImageToVehicle(Long vehicleId, String imageUrl, Boolean isPrimary);
}
