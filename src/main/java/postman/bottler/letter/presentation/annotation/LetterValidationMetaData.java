package postman.bottler.letter.presentation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import postman.bottler.global.response.code.ErrorStatus;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LetterValidationMetaData {
    String message() default "유효성 검사 실패";

    ErrorStatus errorStatus();
}
