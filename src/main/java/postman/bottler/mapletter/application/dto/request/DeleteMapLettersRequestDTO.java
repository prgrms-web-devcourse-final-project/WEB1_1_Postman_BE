package postman.bottler.mapletter.application.dto.request;

import java.util.List;

public record DeleteMapLettersRequestDTO(
        List<Long> letterIds
) {
}
