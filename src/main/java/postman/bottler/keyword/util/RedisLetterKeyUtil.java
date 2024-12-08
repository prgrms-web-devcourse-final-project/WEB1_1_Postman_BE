package postman.bottler.keyword.util;


public class RedisLetterKeyUtil {

    private static final String RECOMMENDATION_PREFIX = "user";
    private static final String RECOMMENDATIONS_SUFFIX = "recommendations";

    public static String getTempRecommendationKey(Long userId) {
        return String.format("%s:%d:%s:temp", RECOMMENDATION_PREFIX, userId, RECOMMENDATIONS_SUFFIX);
    }

    public static String getActiveRecommendationKey(Long userId) {
        return String.format("%s:%d:%s", RECOMMENDATION_PREFIX, userId, RECOMMENDATIONS_SUFFIX);
    }
}
