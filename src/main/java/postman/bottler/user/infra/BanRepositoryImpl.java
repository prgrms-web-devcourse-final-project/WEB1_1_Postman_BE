package postman.bottler.user.infra;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.user.application.repository.BanRepository;
import postman.bottler.user.domain.Ban;
import postman.bottler.user.infra.entity.BanEntity;

@Repository
@RequiredArgsConstructor
public class BanRepositoryImpl implements BanRepository {
    private final BanJpaRepository banRepository;

    @Override
    public Ban save(Ban ban) {
        BanEntity entity = BanEntity.from(ban);
        return banRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Ban> findByUserId(Long userId) {
        Optional<BanEntity> find = banRepository.findById(userId);
        return find.map(BanEntity::toDomain);
    }

    @Override
    public List<Ban> findExpiredBans(LocalDateTime now) {
        return banRepository.findByUnbansAtBefore(now).stream()
                .map(BanEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteBans(List<Ban> bans) {
        banRepository.deleteAll(bans.stream().map(BanEntity::from).toList());
    }

    @Override
    public Ban updateBan(Ban ban) {
        return banRepository.save(BanEntity.from(ban)).toDomain();
    }
}
