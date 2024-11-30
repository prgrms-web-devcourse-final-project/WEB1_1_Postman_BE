package postman.bottler.mapletter.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record MapLetterPageResponseDTO<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> MapLetterPageResponseDTO<T> from(Page<T> page) {
        return new MapLetterPageResponseDTO<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
