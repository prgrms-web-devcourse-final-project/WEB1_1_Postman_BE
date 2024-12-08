package postman.bottler.mapletter.dto;

import java.time.LocalDateTime;

public interface FindSentMapLetter {
    Long getLetterId();
    String getTitle(); //답장 편지의 경우 Re: 답장 편지 이름
    String getDescription(); //답장 편지의 경우 생략
    String getLabel();
    Long getTargetUser(); //타겟 편지의 경우만
    String getType(); //REPLY(답장 편지), TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
    LocalDateTime getCreatedAt();
    Long getSourceLetterId();
}
