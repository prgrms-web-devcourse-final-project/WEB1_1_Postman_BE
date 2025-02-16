package postman.bottler.label.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.label.application.dto.LabelRequestDTO;
import postman.bottler.label.application.dto.LabelResponseDTO;
import postman.bottler.label.exception.EmptyLabelInputException;
import postman.bottler.label.exception.InvalidLabelException;
import postman.bottler.label.application.service.LabelService;
import postman.bottler.label.exception.LabelRequestException;
import postman.bottler.user.auth.CustomUserDetails;

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
    public ApiResponse<String> createLabel(@RequestParam String labelImageUrl,
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
    public ApiResponse<List<LabelResponseDTO>> findAllLabels() {
        List<LabelResponseDTO> labelResponseDTO = labelService.findAllLabels();
        return ApiResponse.onSuccess(labelResponseDTO);
    }

    @Operation(summary = "사용자 라벨 조회", description = "(로그인 필요) 로그인한 사용자가 소유한 라벨을 조회합니다.")
    @GetMapping("/user")
    public ApiResponse<List<LabelResponseDTO>> findUserLabels(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<LabelResponseDTO> labelResponseDTO = labelService.findUserLabels(customUserDetails.getUserId());
        return ApiResponse.onSuccess(labelResponseDTO);
    }

    @Operation(summary = "선착순 라벨 뽑기 대상 라벨 조회", description = "선착순 뽑기 대상 라벨을 조회합니다.")
    @GetMapping("/first-come")
    public ApiResponse<List<LabelResponseDTO>> findFirstComeLabels() {
        List<LabelResponseDTO> labelResponseDTO = labelService.findFirstComeLabels();
        return ApiResponse.onSuccess(labelResponseDTO);
    }

    @Operation(summary = "선착순 라벨 뽑기 대상 라벨 변경 예약", description = "선착순 뽑기 대상 라벨로 변경 예약되었습니다.")
    @PatchMapping("/first-come")
    public ApiResponse<String> updateFirstComeLabel(@Valid @RequestBody LabelRequestDTO labelRequestDTO,
                                                    BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        labelService.updateFirstComeLabel(labelRequestDTO);
        return ApiResponse.onCreateSuccess("선착순 뽑기 대상 라벨로 변경 예약되었습니다.");
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "labelIds", "scheduledDateTime" -> throw new LabelRequestException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }

    @Operation(summary = "선착순 라벨 뽑기", description = "(로그인 필요) 로그인한 사용자가 뽑기 대상 라벨들 중 하나를 선착순으로 가져갑니다.")
    @PostMapping("/first-come")
    public ApiResponse<LabelResponseDTO> createFirstComeFirstServedLabel(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        LabelResponseDTO labelResponseDTO = labelService.createFirstComeFirstServedLabel(customUserDetails.getUserId());
        return ApiResponse.onCreateSuccess(labelResponseDTO);
    }
}
