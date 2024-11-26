package postman.bottler.label.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LabelRequestDTO(@NotBlank(message = "라벨 이미지 URL이 비어 있습니다.") String imageUrl) {
}
