package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import postman.bottler.notification.exception.NoTypeException;

@DisplayName("알림 타입 테스트")
public class NotificationTypeTest {

    @ParameterizedTest(name = "{0}의 편지 관련 알림 체크의 결과는 {1}이다.")
    @CsvSource({"NEW_LETTER, true", "TARGET_LETTER, true", "MAP_REPLY, true", "KEYWORD_REPLY, true",
            "WARNING, false", "BAN, false"})
    @DisplayName("편지 관련 알림인지 확인한다.")
    public void isLetterNotification(NotificationType notificationType, boolean expected) {
        // WHEN
        boolean result = notificationType.isLetterNotification();

        // THEN
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{0}은 {1} 타입으로 변환된다.")
    @CsvSource({"NEW_LETTER, NEW_LETTER", "TARGET_LETTER, TARGET_LETTER", "MAP_REPLY, MAP_REPLY",
            "KEYWORD_REPLY, KEYWORD_REPLY", "WARNING, WARNING", "BAN, BAN"})
    @DisplayName("해당하는 타입의 NotificationType을 반환한다.")
    public void from(String input, NotificationType expected) {
        // WHEN
        NotificationType result = NotificationType.from(input);

        // THEN
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 타입 변환 시도 시, NoTypeException 예외를 발생시킨다.")
    public void noType() {
        // GIVEN
        String nonNotification = "WRONG";

        // WHEN - THEN
        assertThatThrownBy(() -> NotificationType.from(nonNotification)).isInstanceOf(NoTypeException.class);
    }
}
