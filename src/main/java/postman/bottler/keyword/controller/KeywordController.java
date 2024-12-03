package postman.bottler.keyword.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
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
}
