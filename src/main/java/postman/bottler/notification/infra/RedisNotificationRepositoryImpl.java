package postman.bottler.notification.infra;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.service.NotificationRepository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisNotificationRepositoryImpl implements NotificationRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String NOTIFICATION_KEY = "notification";
    private static final long TTL = 7;

    @Override
    public Notification save(Notification notification) {
        RedisNotification redisNotification = RedisNotification.from(notification);
        String notificationKey = NOTIFICATION_KEY + redisNotification.createRedisKey();
        redisTemplate.opsForHash().putAll(notificationKey, redisNotification.toMap());
        redisTemplate.expire(notificationKey, TTL, TimeUnit.DAYS);
        return redisNotification.toDomain();
    }

    @Override
    public Notifications findByReceiver(Long userId) {
        Set<String> keys = redisTemplate.keys(NOTIFICATION_KEY + ":" + userId + ":*");
        List<Notification> notifications = keys.stream()
                .filter(key -> {
                    Long receiver = (Long) redisTemplate.opsForHash().get(key, "receiver");
                    return Objects.equals(receiver, userId);
                })
                .map(key -> RedisNotification.create(redisTemplate, key))
                .map(RedisNotification::toDomain)
                .collect(Collectors.toList());
        return Notifications.from(notifications);
    }

    @Override
    public void updateNotifications(Notifications notifications) {
        notifications.getNotifications().forEach(notification -> {
            RedisNotification redisNotification = RedisNotification.from(notification);
            String notificationKey = NOTIFICATION_KEY + redisNotification.createRedisKey();
            redisTemplate.opsForHash().putAll(notificationKey, redisNotification.toMap());
        });
    }
}
