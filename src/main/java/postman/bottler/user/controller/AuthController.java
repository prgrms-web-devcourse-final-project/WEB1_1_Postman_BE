package postman.bottler.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.request.SignUpRequestDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.createUser(signUpRequestDTO.email(), signUpRequestDTO.password(), signUpRequestDTO.nickname());
        return ApiResponse.onCreateSuccess("회원가입 성공");
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "email" -> throw new EmailException(error.getDefaultMessage());
                    case "password" -> throw new PasswordException(error.getDefaultMessage());
                    case "nickname" -> throw new NicknameException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }
}
