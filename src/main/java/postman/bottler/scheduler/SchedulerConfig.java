package postman.bottler.scheduler;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final RecommendationScheduler recommendationScheduler;
    private final UnbanScheduler unbanScheduler;

    @Scheduled(cron = "0 0 0 * * ?")
    public void unban() {
        unbanScheduler.unbanUsers(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 23,11,17 * * *")
    public void executeRecommendationJob() {
        recommendationScheduler.processAllUserRecommendations();
    }

    @Scheduled(cron = "0 0 0,12,18 * * ?")
    public void recommendKeywordLetter() {
        recommendationScheduler.updateAllRecommendations();
    }
}
