package postman.bottler.keyword.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.keyword.dto.request.UserKeywordRequestDTO;
import postman.bottler.keyword.dto.response.UserKeywordResponseDTO;
import postman.bottler.keyword.service.UserKeywordService;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
public class KeywordController {

    private final UserKeywordService userKeywordService;

    @GetMapping
    public ApiResponse<UserKeywordResponseDTO> getKeywords(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UserKeywordResponseDTO result = userKeywordService.getKeywords(userDetails.getUserId());
        return ApiResponse.onSuccess(result);
    }

    @PostMapping
    public ApiResponse<String> createKeywords(
            @RequestBody UserKeywordRequestDTO userKeywordRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        userKeywordService.createKeywords(userKeywordRequestDTO, userDetails.getUserId());
        return ApiResponse.onSuccess("사용자 키워드를 생성하였습니다.");
    }
}
