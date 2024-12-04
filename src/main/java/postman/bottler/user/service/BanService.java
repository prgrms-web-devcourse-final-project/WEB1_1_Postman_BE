package postman.bottler.user.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.user.domain.Ban;
import postman.bottler.user.domain.Role;
import postman.bottler.user.domain.User;

@Service
@RequiredArgsConstructor
public class BanService {
    private static final Long BAN_DAYS = 7L;

    private final BanRepository banRepository;
    private final UserRepository userRepository;

    public void unbanExpiredUsers() {
        List<Ban> unbanned = banRepository.findUnbanned(LocalDateTime.now());
        /** TODO 유저 정지 해제
         *  Users users = userRepository.findWillBeUnbannedUser(unbanned);
         *  users.unbanned();
         */
        /** TODO 유저 정보 업데이트
         * userRepository.updateUsers(users);
         */
        banRepository.deleteBans(unbanned);
    }

    public void banUser(User user) {
//        if (user.isBanned()) {
//            Ban ban = banRepository.findByUserId(userId).orElseThrow();
//            ban.extendBanDuration(BAN_DAYS);
//            banRepository.updateBan(ban);
//            return;
//        }
//        Ban ban = Ban.create(userId, BAN_DAYS);
//        user.banned();
//        banRepository.save(ban);
//        userRepository.update(user);
    }
}
