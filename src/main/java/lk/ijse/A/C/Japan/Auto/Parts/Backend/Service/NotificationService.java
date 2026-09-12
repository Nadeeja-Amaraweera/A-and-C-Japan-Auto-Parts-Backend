package lk.ijse.A.C.Japan.Auto.Parts.Backend.Service;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO.NotificationDTO;
import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.NotificationType;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getUserNotifications(Long userId);
    NotificationDTO markAsRead(Long notificationId);
    NotificationDTO createNotification(Long userId, String title, String message, NotificationType type, String targetUrl);
    Long getUnreadCount(Long userId);
}
