package postman.bottler.keyword.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.keyword.applications.service.KeywordService;
import postman.bottler.keyword.applications.service.LetterKeywordService;
import postman.bottler.keyword.applications.service.UserKeywordService;
import postman.bottler.keyword.dto.request.UserKeywordRequestDTO;
import postman.bottler.keyword.dto.response.FrequentKeywordsDTO;
import postman.bottler.keyword.dto.response.KeywordResponseDTO;
import postman.bottler.keyword.dto.response.UserKeywordResponseDTO;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
public class KeywordController {

    private final UserKeywordService userKeywordService;
    private final KeywordService keywordService;
    private final LetterKeywordService letterKeywordService;

    @Operation(
            summary = "유저 키워드 목록 조회",
            description = "유저가 설정한 키워드 목록을 조회합니다."
    )
    @GetMapping
    public ApiResponse<UserKeywordResponseDTO> getKeywords(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserKeywordResponseDTO result = userKeywordService.getUserKeywords(userDetails.getUserId());
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "유저 키워드 등록",
            description = "유저가 설정한 키워드로 변경합니다."
    )
    @PostMapping
    public ApiResponse<String> createKeywords(
            @RequestBody UserKeywordRequestDTO userKeywordRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userKeywordService.createKeywords(userKeywordRequestDTO, userDetails.getUserId());
        return ApiResponse.onSuccess("사용자 키워드를 생성하였습니다.");
    }

    @Operation(
            summary = "전체 키워드 조회",
            description = "카테고리별로 등록된 키워드 목록을 조회합니다."
    )
    @GetMapping("/list")
    public ApiResponse<KeywordResponseDTO> getKeywordList() {
        KeywordResponseDTO result = keywordService.getKeywords();
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "사용자의 자주 쓰는 키워드 조회",
            description = "현재 사용자의 자주 쓰는 키워드를 조회합니다"
    )
    @GetMapping("/frequent")
    public ApiResponse<FrequentKeywordsDTO> getTopFrequentKeywords(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        FrequentKeywordsDTO result = letterKeywordService.getTopFrequentKeywords(userDetails.getUserId());
        return ApiResponse.onSuccess(result);
    }
}
