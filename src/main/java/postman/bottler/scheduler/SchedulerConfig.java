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

    @Scheduled(cron = "0 0 9/21 * * ?")
    public void sendKeywordLetterToAll() {
        // TODO 키워드 편지 보내기
        // TODO 모든 유저에게 알림 전송
    }

    //
    @Scheduled(cron = "0 0 0 * * ?")
    public void unbanUsers() {
        // TODO 정지 기한이 지난 유저들 조회
        // TODO 정지 기한이 지난 유저 정지 해제
        // TODO 정지 정보 삭제
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void executeRecommendationJob() {
        recommendationScheduler.processAllUserRecommendations();
    }
}
