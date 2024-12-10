package postman.bottler.keyword.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.keyword.service.AsyncRecommendationService;
import postman.bottler.keyword.service.RedisLetterService;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test/recommendations")
@Tag(name = "테스트용")
public class RecommendationController {

    private final AsyncRecommendationService asyncRecommendationService;
    private final RedisLetterService redisLetterService;
    private final UserService userService;

    @Operation(
            summary = "키워드 편지 추천 요청",
            description = "현재 3개가 추천됩니다"
    )
    @PostMapping("/process")
    public ResponseEntity<String> processRecommendation() {
        // 비동기 추천 작업 시작
        List<Long> userIds = userService.getAllUserIds();
        userIds.forEach(asyncRecommendationService::processRecommendationForUser);

        return ResponseEntity.ok("Recommendation process started for user " + userIds);
    }

    @Operation(
            summary = "추천 된 편지 조회 요청",
            description = "현재 추천된 편지의 id 들을 반환합니다"
    )
    @GetMapping("/result")
    public ResponseEntity<Map<Long, List<Long>>> getRecommendationResult() {
        List<Long> userIds = userService.getAllUserIds();
        Map<Long, List<Long>> result = new HashMap<>();
        userIds.forEach(userId ->
                result.put(userId, redisLetterService.getRecommendations(userId))
        );

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "업데이트된 추천 편지 변경 요청",
            description = "기존 추천된 편지 중 가장 오래된 편지를 밀어내고 추천 후보 중 조건에 맞는 편지 id를 등록해줍니다."
    )
    @PostMapping("/update")
    public ResponseEntity<String> updateRecommendationsFromTemp() {
        List<Long> userIds = userService.getAllUserIds();
        try {
            userIds.forEach(redisLetterService::updateRecommendationsFromTemp);
            return ResponseEntity.ok("추천 키워드 편지 변경 완료");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update recommendations: " + e.getMessage());
        }
    }

    @Operation(
            summary = "추천될 키워드 편지 id 정보들 조회 요청",
            description = "사용자에게 제공된 추천 편지가 아닌 추천될 편지의 아이디 목록입니다."
                    + "\n 추천할 때 편지가 삭제되어 있을 가능성이 있기 떄문에 검증 후 순차적으로 이중 하나를 사용자에게 추천합니다."
    )
    @GetMapping("/temp")
    public ResponseEntity<Map<Long, List<Long>>> getRecommendTemp() {
        List<Long> userIds = userService.getAllUserIds();
        Map<Long, List<Long>> result = new HashMap<>();
        userIds.forEach(userId -> result.put(userId, redisLetterService.getRecommendations(userId))
        );
        return ResponseEntity.ok(result);
    }
}
