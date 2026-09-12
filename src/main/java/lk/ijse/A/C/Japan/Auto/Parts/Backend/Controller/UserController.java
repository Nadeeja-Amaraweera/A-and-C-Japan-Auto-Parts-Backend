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

    @PostMapping(value = "/{userId}/become-supplier", produces = MediaType.APPLICATION_JSON_VALUE)
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
