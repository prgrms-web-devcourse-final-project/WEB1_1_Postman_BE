package postman.bottler.scheduler;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.service.AsyncRecommendationService;
import postman.bottler.keyword.service.RedisLetterService;
import postman.bottler.notification.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.user.service.UserService;

@Service
@RequiredArgsConstructor
public class RecommendationScheduler {

    private final AsyncRecommendationService asyncRecommendationService;
    private final UserService userService;
    private final RedisLetterService redisLetterService;

    public void processAllUserRecommendations() {
        List<Long> userIds = userService.getAllUserIds();
        userIds.forEach(asyncRecommendationService::processRecommendationForUser);
    }

    public void updateAllRecommendations() {
        List<Long> userIds = userService.getAllUserIds(); // 사용자 ID 목록 가져오는 메서드
        // 알림전송
        for (Long userId : userIds) {
            RecommendNotificationRequestDTO recommendNotificationRequestDTO =
                    redisLetterService.updateRecommendationsFromTemp(userId);
        }
    }
}
