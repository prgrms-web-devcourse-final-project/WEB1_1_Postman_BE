package postman.bottler.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {
    private final RecommendationScheduler recommendationScheduler;

    @Scheduled(cron = "0 0 0 * * ?")
    public void unban() {
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void executeRecommendationJob() {
        recommendationScheduler.processAllUserRecommendations();
    }
}
