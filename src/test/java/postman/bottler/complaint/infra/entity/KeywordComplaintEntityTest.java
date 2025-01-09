package postman.bottler.complaint.infra.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.complaint.domain.Complaint;

@DisplayName("키워드 편지 JPA 엔티티 테스트")
class KeywordComplaintEntityTest {

    @DisplayName("도메인 엔티티로부터 JPA 엔티티를 생성한다.")
    @Test
    void from() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Complaint complaint = Complaint.of(1L, 1L, 1L, "욕설 사용", now);

        // when
        KeywordComplaintEntity jpaEntity = KeywordComplaintEntity.from(complaint);

        // then
        assertThat(jpaEntity)
                .extracting("id", "letterId", "reporterId", "description", "createdAt")
                .contains(1L, 1L, 1L, "욕설 사용", now);
    }

    @DisplayName("JPA 엔티티로부터 도메인 엔티티를 생성한다.")
    @Test
    void toDomain() {
        // given
        LocalDateTime now = LocalDateTime.now();
        KeywordComplaintEntity jpaEntity = KeywordComplaintEntity.builder()
                .id(1L)
                .letterId(1L)
                .reporterId(1L)
                .description("욕설 사용")
                .createdAt(now)
                .build();

        // when
        Complaint domainEntity = jpaEntity.toDomain();

        // then
        assertThat(domainEntity)
                .extracting("id", "letterId", "reporterId", "description", "createdAt")
                .contains(1L, 1L, 1L, "욕설 사용", now);
    }
}
