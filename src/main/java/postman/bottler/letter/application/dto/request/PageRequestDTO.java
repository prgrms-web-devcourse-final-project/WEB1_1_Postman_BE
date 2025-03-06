package postman.bottler.letter.application.dto.request;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import postman.bottler.letter.domain.SortField;

public record PageRequestDTO(@Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.") Integer page,
                             @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.") Integer size, String sort) {
    public PageRequestDTO {
        page = (page == null) ? 1 : page;
        size = (size == null) ? 9 : size;
        sort = (sort == null || sort.isBlank()) ? "createdAt" : sort;

        SortField.validateSort(sort);
    }

    public Pageable toPageable() {
        return PageRequest.of(page - 1, size, Sort.by(sort).descending());
    }
}
