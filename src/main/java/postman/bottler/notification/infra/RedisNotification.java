package postman.bottler.notification.infra;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisTemplate;
import postman.bottler.notification.domain.LetterNotification;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RedisNotification {
    @Id
    private UUID id;

    private Long receiver;

    private NotificationType type;

    private Long letterId;

    private LocalDateTime createdAt;

    private Boolean isRead;

    public static RedisNotification from(Notification notification) {
        return RedisNotification.builder()
                .id(notification.getId())
                .receiver(notification.getReceiver())
                .type(notification.getType())
                .letterId(notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId()
                        : null)
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .build();
    }

    public Notification toDomain() {
        return Notification.of(id, type, receiver, letterId, createdAt, isRead);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("receiver", receiver);
        map.put("type", type);
        map.put("letterId", letterId);
        map.put("createdAt", createdAt);
        map.put("isRead", isRead);
        return map;
    }

    public static RedisNotification create(RedisTemplate<String, Object> redisTemplate, String key) {
        return RedisNotification.builder()
                .id((UUID) redisTemplate.opsForHash().get(key, "id"))
                .type((NotificationType) redisTemplate.opsForHash().get(key, "type"))
                .receiver((Long) redisTemplate.opsForHash().get(key, "receiver"))
                .createdAt((LocalDateTime) redisTemplate.opsForHash().get(key, "createdAt"))
                .letterId((Long) redisTemplate.opsForHash().get(key, "letterId"))
                .isRead((Boolean) redisTemplate.opsForHash().get(key, "isRead"))
                .build();
    }

    public String createRedisKey() {
        return ":" + receiver + ":" + id;
    }
}
