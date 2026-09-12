package lk.ijse.A.C.Japan.Auto.Parts.Backend.Controller;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.CommonResponse;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.NotificationDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.A.C.Japan.Auto.Parts.Backend.Constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "*"})
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUserNotifications(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getUserNotifications(userId);
        return new CommonResponse(OPERATION_SUCCESS, notifications, SUCCESS_MESSAGE);
    }

    @PutMapping(value = "/{id}/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse markAsRead(@PathVariable Long id) {
        NotificationDTO notification = notificationService.markAsRead(id);
        return new CommonResponse(OPERATION_SUCCESS, notification, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/user/{userId}/unread-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getUnreadCount(@PathVariable Long userId) {
        Long count = notificationService.getUnreadCount(userId);
        return new CommonResponse(OPERATION_SUCCESS, count, SUCCESS_MESSAGE);
    }
}
