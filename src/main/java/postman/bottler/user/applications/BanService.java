package postman.bottler.user.applications;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.user.applications.repository.BanRepository;
import postman.bottler.user.applications.repository.UserRepository;
import postman.bottler.user.domain.Ban;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.UserBanException;

@Service
@RequiredArgsConstructor
public class BanService {
    private static final Long BAN_DAYS = 7L;

    private final BanRepository banRepository;
    private final UserRepository userRepository;

    public void banUser(User user) {
        if (user.isBanned()) {
            Ban ban = banRepository.findByUserId(user.getUserId())
                    .orElseThrow(() -> new UserBanException("정지된 유저가 아닙니다."));
            ban.extendBanDuration(BAN_DAYS);
            banRepository.updateBan(ban);
            return;
        }
        Ban ban = Ban.create(user.getUserId(), BAN_DAYS);
        user.banned();
        banRepository.save(ban);
    }

    @Transactional
    public void unbans(LocalDateTime now) {
        List<Ban> expiredBans = banRepository.findExpiredBans(now);
        List<User> willBeUnbanned = userRepository.findWillBeUnbannedUsers(expiredBans);
        willBeUnbanned.forEach(User::unban);
        userRepository.updateUsers(willBeUnbanned);
        banRepository.deleteBans(expiredBans);
    }
}
