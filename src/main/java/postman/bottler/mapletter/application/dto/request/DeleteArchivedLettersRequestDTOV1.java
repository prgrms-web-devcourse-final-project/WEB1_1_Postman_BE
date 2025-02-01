package postman.bottler.mapletter.application.dto.request;

import java.util.List;

public record DeleteArchivedLettersRequestDTOV1( //삭제 예정
        List<Long> archiveIds
) {
}
