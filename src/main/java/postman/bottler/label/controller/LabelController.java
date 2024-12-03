package postman.bottler.label.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "라벨", description = "라벨 관련 API")
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @Operation(summary = "라벨 생성", description = "라벨 이미지 URL을 DB에 저장합니다. 실제 서비스에서 사용하는 API는 아닙니다!")
    @PostMapping
    public ApiResponse<?> createLabel(@RequestParam String labelImageUrl,
                                      @RequestParam(required = false) Integer limitCount) {
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

    @Operation(summary = "전체 라벨 조회", description = "(로그인 필요) 모든 라벨을 조회합니다.")
    @GetMapping
    public ApiResponse<?> findAllLabels() {
        List<LabelResponseDTO> labelResponseDTO = labelService.findAllLabels();
        return ApiResponse.onSuccess(labelResponseDTO);
    }

    @Operation(summary = "사용자 라벨 조회", description = "(로그인 필요) 로그인한 사용자가 소유한 라벨을 조회합니다.")
    @GetMapping("/user")
    public ApiResponse<?> findUserLabels(Long userId) { // TODO: 유저 로직 구현 후 변경 예정
        List<LabelResponseDTO> labelResponseDTO = labelService.findUserLabels(userId);
        return ApiResponse.onSuccess(labelResponseDTO);
    }

    @Operation(summary = "선착순 라벨 뽑기", description = "(로그인 필요) 로그인한 사용자가 입력된 라벨 ID에 해당하는 라벨을 선착순으로 가져갑니다.")
    @PostMapping("/{labelId}")
    public ApiResponse<?> createFirstComeFirstServedLabel(@PathVariable Long labelId,
                                                          Long userId) {  // TODO: 유저 로직 구현 후 변경 예정
        labelService.createFirstComeFirstServedLabel(labelId, userId);
        return ApiResponse.onCreateSuccess("선착순 라벨 뽑기 성공");
    }
}
