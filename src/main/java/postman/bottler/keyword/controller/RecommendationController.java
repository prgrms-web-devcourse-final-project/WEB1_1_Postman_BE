package postman.bottler.keyword.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.keyword.service.AsyncRecommendationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final AsyncRecommendationService asyncRecommendationService;
    private final RedisTemplate<String, List<Long>> redisTemplate;

    @PostMapping("/process/{userId}")
    public ResponseEntity<String> processRecommendation(@PathVariable Long userId) {
        // 비동기 추천 작업 시작
        asyncRecommendationService.processRecommendationForUser(userId);

        return ResponseEntity.ok("Recommendation process started for user " + userId);
    }

    @GetMapping("/result/{userId}")
    public ResponseEntity<List<Long>> getRecommendationResult(@PathVariable Long userId) {
        // Redis에서 추천 결과 조회
        String redisKey = "user:" + userId + ":recommendations";
        List<Long> recommendations = redisTemplate.opsForValue().get(redisKey);

        if (recommendations == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.ok(recommendations);
    }
}
