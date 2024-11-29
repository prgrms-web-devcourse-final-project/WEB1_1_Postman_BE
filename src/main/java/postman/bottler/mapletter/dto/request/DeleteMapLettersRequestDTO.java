package postman.bottler.mapletter.dto.request;

import java.util.List;

public record DeleteMapLettersRequestDTO(
        List<Long> letterIds
) {
}
