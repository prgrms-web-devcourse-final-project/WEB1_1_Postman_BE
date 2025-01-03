package postman.bottler.scheduler;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import postman.bottler.user.application.service.BanService;

@Component
@RequiredArgsConstructor
public class UnbanScheduler {
    private final BanService banService;

    public void unbanUsers(LocalDateTime unbansAt) {
        banService.unbans(unbansAt);
    }
}
