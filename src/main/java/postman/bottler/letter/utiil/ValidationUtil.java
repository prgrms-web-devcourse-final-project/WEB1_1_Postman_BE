package postman.bottler.letter.utiil;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ValidationUtil {
    public <T extends RuntimeException> void validate(
            BindingResult bindingResult,
            Function<Map<String, String>, T> exceptionSupplier
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "검증 실패")
                    ));
            throw exceptionSupplier.apply(errors);
        }
    }
}
