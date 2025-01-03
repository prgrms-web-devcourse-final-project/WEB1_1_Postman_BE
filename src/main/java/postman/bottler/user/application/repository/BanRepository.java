package postman.bottler.user.application.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import postman.bottler.user.domain.Ban;

public interface BanRepository {
    Ban save(Ban ban);

    Optional<Ban> findByUserId(Long userId);

    List<Ban> findExpiredBans(LocalDateTime now);

    void deleteBans(List<Ban> bans);

    Ban updateBan(Ban ban);
}
