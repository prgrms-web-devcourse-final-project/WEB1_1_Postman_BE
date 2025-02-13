package postman.bottler.letter.exception;

import postman.bottler.global.response.code.ErrorStatus;

public class TempRecommendationsNotFoundException extends LetterCustomException {
    public TempRecommendationsNotFoundException(String key) {
        super(ErrorStatus.TEMP_RECOMMENDATIONS_NOT_FOUND, "추천 데이터가 없습니다. key: " + key);
    }
}
