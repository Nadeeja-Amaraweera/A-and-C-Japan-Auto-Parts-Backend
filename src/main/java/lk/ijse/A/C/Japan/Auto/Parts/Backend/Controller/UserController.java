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
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse registerUser(@RequestBody UserDTO userDTO) {
        UserDTO saveUser = userService.saveUser(userDTO);
        return new CommonResponse(OPERATION_SUCCESS,saveUser,SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse authLogin(@RequestBody AuthDTO authDTO){
        UserDTO userDetails = userService.getUserDetails(authDTO.getUserEmail(), authDTO.getPassword());
        System.out.println("API called here");
        String token = jwtUtil.generateToken(userDetails);
        UserDataDTO userDataDTO = new UserDataDTO();
        userDataDTO.setUserId(userDetails.getUserId());
        userDataDTO.setUserName(userDetails.getUserName());
        userDataDTO.setToken(token);
        return new CommonResponse(0, userDataDTO, "JWT Token generated successfully");
    }
}
