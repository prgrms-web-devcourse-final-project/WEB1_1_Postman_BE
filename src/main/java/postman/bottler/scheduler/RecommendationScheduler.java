package postman.bottler.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.service.AsyncRecommendationService;
import postman.bottler.user.service.UserService;

@Service
@RequiredArgsConstructor
public class RecommendationScheduler {

    private final AsyncRecommendationService asyncRecommendationService;
    private final UserService userService;

    public void processAllUserRecommendations() {
        // 모든 유저조회
        /*
            List<Long> allUserIds = userService.getAllUserIds;
            allUserIds.foreach(asyncRecommendationService::processRecommendationForUser)
         */
        asyncRecommendationService.processRecommendationForUser(1L);
    }
}
