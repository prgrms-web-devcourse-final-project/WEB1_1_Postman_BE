package postman.bottler.mapletter.dto.request;

import java.util.List;

public record DeleteArchivedLettersRequestDTO(
        List<Long> archiveIds
) {
}
