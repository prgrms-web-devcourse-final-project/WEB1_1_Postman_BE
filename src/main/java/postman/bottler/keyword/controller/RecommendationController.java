package postman.bottler.keyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.keyword.service.AsyncRecommendationService;
import postman.bottler.keyword.service.RedisLetterService;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/recommendations")
public class RecommendationController {

    private final AsyncRecommendationService asyncRecommendationService;
    private final RedisLetterService redisLetterService;

    @Operation(
            summary = "키워드 편지 추천 요청",
            description = "현재 3개가 추천됩니다"
    )
    @PostMapping("/process")
    public ResponseEntity<String> processRecommendation(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 비동기 추천 작업 시작
        asyncRecommendationService.processRecommendationForUser(userDetails.getUserId());

        return ResponseEntity.ok("Recommendation process started for user " + userDetails.getUserId());
    }

    @Operation(
            summary = "추천 된 편지 조회 요청",
            description = "현재 추천된 편지의 id 들을 반환합니다"
    )
    @GetMapping("/result")
    public ResponseEntity<List<Long>> getRecommendationResult(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<Long> result = redisLetterService.getRecommendations(userDetails.getUserId());

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "업데이트된 추천 편지 변경 요청",
            description = "다시 추천을 요청할 경우 이 요청을 수행해야 새로 추천된 편지로 변경됩니다."
    )
    @PostMapping("/update")
    public ResponseEntity<String> updateRecommendationsFromTemp(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            redisLetterService.updateRecommendationsFromTemp(userDetails.getUserId());
            return ResponseEntity.ok("추천 키워드 편지 변경 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update recommendations: " + e.getMessage());
        }
    }


}
