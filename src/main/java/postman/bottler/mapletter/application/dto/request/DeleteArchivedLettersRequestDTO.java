package postman.bottler.mapletter.application.dto.request;

import java.util.List;

public record DeleteArchivedLettersRequestDTO(
        List<Long> archiveIds
) {
}
