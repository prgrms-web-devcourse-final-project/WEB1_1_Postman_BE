package postman.bottler.mapletter.application.dto.request;

import java.util.List;

public record DeleteMapLettersV1RequestDTO(
        List<Long> letterIds
) {
}
