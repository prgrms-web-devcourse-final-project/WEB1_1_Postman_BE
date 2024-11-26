package postman.bottler.label.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.label.dto.request.LabelRequestDTO;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.exception.EmptyLabelInputException;
import postman.bottler.label.service.LabelService;

@RestController
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public ApiResponse<?> createLabel(@RequestParam String labelImageUrl) {
        if (labelImageUrl == null || labelImageUrl.trim().isEmpty()) {
            throw new EmptyLabelInputException("라벨 이미지 URL이 비어 있습니다.");
        }
        labelService.createLabel(labelImageUrl);
        return ApiResponse.onCreateSuccess("라벨 추가 성공");
    }

    @GetMapping
    public ApiResponse<?> findAllLabels() {
        List<LabelResponseDTO> labelResponseDTO = labelService.findAllLabels();
        return ApiResponse.onSuccess(labelResponseDTO);
    }

    @GetMapping("/user")
    public ApiResponse<?> findUserLabels(Long userId) {
        List<LabelResponseDTO> labelResponseDTO = labelService.findUserLabels(userId);
        return ApiResponse.onSuccess(labelResponseDTO);
    }
}
