package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.AuthDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.UserDataDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Security.JwtUtil;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.SupplierService supplierService;

    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse registerUser(@RequestBody UserDTO userDTO) {
        UserDTO saveUser = userService.saveUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS,saveUser,SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateUser(@RequestBody UserDTO userDTO) {
        UserDTO saveUser = userService.updateUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS,saveUser,SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/delete/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new CommonResponse(OPERATION_SUCCESS, null, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/get/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserById(@PathVariable long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return new CommonResponse(OPERATION_SUCCESS, userDTO, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getUserByEmail(email);
        return new CommonResponse(OPERATION_SUCCESS, userDTO, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/{userId}/become-supplier", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse becomeSupplierMultipart(
            @PathVariable Long userId,
            @RequestParam(value = "businessRegistrationDocument", required = false) org.springframework.web.multipart.MultipartFile businessRegistrationDocument,
            @RequestParam(value = "file", required = false) org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "document", required = false) org.springframework.web.multipart.MultipartFile document,
            @RequestParam(value = "businessName", required = false) String businessName,
            @RequestParam(value = "supplierBusinessName", required = false) String supplierBusinessName,
            @RequestParam(value = "businessAddress", required = false) String businessAddress,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "supplierBusinessAddress", required = false) String supplierBusinessAddress,
            @RequestParam(value = "contactPerson", required = false) String contactPerson,
            @RequestParam(value = "supplierName", required = false) String supplierName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "supplierContactNumber", required = false) String supplierContactNumber) {

        org.springframework.web.multipart.MultipartFile docFile = businessRegistrationDocument != null ? businessRegistrationDocument
                : (file != null ? file : document);

        lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO supplierDTO = new lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO();
        supplierDTO.setUserId(userId);
        supplierDTO.setSupplierBusinessName(businessName != null ? businessName : supplierBusinessName);
        supplierDTO.setSupplierBusinessAddress(businessAddress != null ? businessAddress : (address != null ? address : supplierBusinessAddress));
        supplierDTO.setSupplierName(contactPerson != null ? contactPerson : supplierName);
        supplierDTO.setSupplierContactNumber(phoneNumber != null ? phoneNumber : (phone != null ? phone : supplierContactNumber));

        lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO result = supplierService.applyToBeSupplierWithDocument(supplierDTO, docFile);
        return new CommonResponse(OPERATION_SUCCESS, result, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/{userId}/become-supplier", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse becomeSupplier(@PathVariable Long userId, @RequestBody lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO supplierDTO) {
        supplierDTO.setUserId(userId);
        lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO result = supplierService.applyToBeSupplier(supplierDTO);
        return new CommonResponse(OPERATION_SUCCESS, result, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{userId}/supplier-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getSupplierStatus(@PathVariable Long userId) {
        lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.SupplierDTO result = supplierService.getSupplierByUserId(userId);
        return new CommonResponse(OPERATION_SUCCESS, result, SUCCESS_MESSAGE);
    }
}
