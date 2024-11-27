package postman.bottler.label.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.exception.EmptyLabelInputException;
import postman.bottler.label.exception.InvalidLabelException;
import postman.bottler.label.service.LabelService;

@RestController
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public ApiResponse<?> createLabel(@RequestParam String labelImageUrl, @RequestParam(required = false) Integer limitCount) {
        validateLabelImageUrl(labelImageUrl);
        limitCount = validateLimitCount(limitCount);

        labelService.createLabel(labelImageUrl, limitCount);
        return ApiResponse.onCreateSuccess("라벨 추가 성공");
    }

    private void validateLabelImageUrl(String labelImageUrl) {
        if (labelImageUrl == null || labelImageUrl.trim().isEmpty()) {
            throw new EmptyLabelInputException("라벨 이미지 URL이 비어 있습니다.");
        }
    }

    private Integer validateLimitCount(Integer limitCount) {
        if (limitCount == null) {
            limitCount = Integer.MAX_VALUE;
        }
        if (limitCount < 0) {
            throw new InvalidLabelException("선착순으로 라벨을 받을 수 있는 최대 인원 수는 0보다 작을 수 없습니다.");
        }
        return limitCount;
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

    @PostMapping("/{labelId}/{userId}")
    public ApiResponse<?> createFirstComeFirstServedLabel(@PathVariable Long labelId, @PathVariable Long userId) {
        labelService.createFirstComeFirstServedLabel(labelId, userId);
        return ApiResponse.onCreateSuccess("선착순 라벨 뽑기 성공");
    }
}
