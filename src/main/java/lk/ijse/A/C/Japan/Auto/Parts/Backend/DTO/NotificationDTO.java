package lk.ijse.A.C.Japan.Auto.Parts.Backend.DTO;

import lk.ijse.A.C.Japan.Auto.Parts.Backend.Enumaration.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long notificationId;
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
    private String relatedEntityType;
    private Long relatedEntityId;
    private String targetUrl;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
