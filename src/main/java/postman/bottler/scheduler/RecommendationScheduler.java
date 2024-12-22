package postman.bottler.scheduler;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.service.AsyncRecommendationService;
import postman.bottler.keyword.service.RedisLetterService;
import postman.bottler.notification.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.notification.service.NotificationService;
import postman.bottler.user.applications.UserService;

@Service
@RequiredArgsConstructor
public class RecommendationScheduler {

    private final AsyncRecommendationService asyncRecommendationService;
    private final UserService userService;
    private final RedisLetterService redisLetterService;
    private final NotificationService notificationService;

    public void processAllUserRecommendations() {
        List<Long> userIds = userService.getAllUserIds();
        userIds.forEach(asyncRecommendationService::processRecommendationForUser);
    }

    public void updateAllRecommendations() {
        List<Long> userIds = userService.getAllUserIds(); // 사용자 ID 목록 가져오는 메서드
        // 알림전송
        List<RecommendNotificationRequestDTO> recommendNotificationRequestDTOS = new ArrayList<>();
        userIds.forEach(userId ->
                {
                    RecommendNotificationRequestDTO recommendNotificationRequestDTO = redisLetterService.updateRecommendationsFromTemp(
                            userId);
                    if (recommendNotificationRequestDTO != null) {
                        recommendNotificationRequestDTOS.add(recommendNotificationRequestDTO);
                    }
                }
        );
        notificationService.sendKeywordNotifications(recommendNotificationRequestDTOS);
    }
}
