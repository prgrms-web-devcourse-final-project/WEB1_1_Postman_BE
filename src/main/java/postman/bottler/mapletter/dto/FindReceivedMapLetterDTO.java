package postman.bottler.mapletter.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FindReceivedMapLetterDTO {
    Long getLetterId();

    String getTitle();

    String getDescription();

    BigDecimal getLatitude();

    BigDecimal getLongitude();

    String getLabel();

    String getType(); // "TARGET", "REPLY"

    Long getSourceLetterId(); // 답장 편지의 경우만 존재

    LocalDateTime getCreatedAt();
}
