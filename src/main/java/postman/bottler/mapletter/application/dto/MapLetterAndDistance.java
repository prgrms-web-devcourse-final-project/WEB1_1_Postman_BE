package postman.bottler.mapletter.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MapLetterAndDistance {
    Long getLetterId();

    BigDecimal getLatitude();

    BigDecimal getLongitude();

    String getTitle();

    LocalDateTime getCreatedAt();

    BigDecimal getDistance();

    Long getTargetUserId();

    Long getCreateUserId();

    String getLabel();

    String getDescription();

}
