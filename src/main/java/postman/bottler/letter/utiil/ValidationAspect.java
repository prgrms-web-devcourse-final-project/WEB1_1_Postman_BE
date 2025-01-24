package postman.bottler.letter.utiil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import postman.bottler.letter.exception.LetterValidationException;
import postman.bottler.letter.presentation.annotation.LetterValidationMetaData;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {

    private final ValidationUtil validationUtil;

    @Before("@annotation(letterValidationMetadata) && args(*, bindingResult, ..)")
    public void validateRequest(BindingResult bindingResult, LetterValidationMetaData letterValidationMetadata) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            validationUtil.validate(bindingResult, errors ->
                    new LetterValidationException(
                            letterValidationMetadata.message(),
                            errors,
                            letterValidationMetadata.errorStatus()
                    )
            );
        }
    }
}
