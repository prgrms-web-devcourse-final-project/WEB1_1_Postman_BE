package postman.bottler.mapletter.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FindNearbyLettersResponse(
        Long letterId,
        BigDecimal latitude,
        BigDecimal longitude,
        String title,
        LocalDateTime createdAt,
        BigDecimal distance,
        Long target,
        Long createUserId,
        String label
) {
}
