package postman.bottler.user.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import postman.bottler.user.domain.Ban;

@RequiredArgsConstructor
public class BanService {
    private static final Long BAN_DAYS = 7L;

    private final BanRepository banRepository;

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

    public void banUser(Long userId) {
        /** TODO 유저 정지
         * User user = userRepository.findById(userId);
         * if (user.isBanned()) {
         *     Ban ban = banRepository.findById(userId).orElseThrow()
         *     ban.extendBanDuration(BAN_DAYS);
         *     banRepository.update(ban);
         *     return;
         * }
         * Ban ban = Ban.create(userId, BAN_DAYS);
         * user.banned();
         * banRepository.save(ban);
         * userRepository.update(user);
         */
    }
}
