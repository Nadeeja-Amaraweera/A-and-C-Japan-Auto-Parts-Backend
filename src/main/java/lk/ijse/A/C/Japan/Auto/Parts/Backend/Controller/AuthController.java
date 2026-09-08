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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

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

    @GetMapping("/validate")
    public CommonResponse validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);

            if (jwtUtil.isTokenExpired(token)) {
                return new CommonResponse(1, "Token expired");
            }

            String username = jwtUtil.extractUsername(token);
            UserDTO user = userService.getUserByUsername(username);

            if (user == null) {
                return new CommonResponse(1, "User not found");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUserId());
            data.put("username", user.getUserName());
            data.put("expiresAt", jwtUtil.extractExpiration(token));

            return new CommonResponse(0, data, "Token is valid");
        } catch (Exception e) {
            return new CommonResponse(1, "Invalid token");
        }
    }
}
