package postman.bottler.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.request.CheckDuplicateEmailRequestDTO;
import postman.bottler.user.dto.request.CheckDuplicateNicknameRequestDTO;
import postman.bottler.user.dto.request.SignUpRequestDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<?> signup(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.createUser(signUpRequestDTO.email(), signUpRequestDTO.password(), signUpRequestDTO.nickname());
        return ApiResponse.onCreateSuccess("회원가입 성공");
    }

    @PostMapping("/duplicate-check/email")
    public ApiResponse<?> checkDuplicateEmail(@Valid @RequestBody CheckDuplicateEmailRequestDTO checkDuplicateEmailRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.checkEmail(checkDuplicateEmailRequestDTO.email());
        return ApiResponse.onSuccess("사용 가능한 이메일입니다.");
    }

    @PostMapping("/duplicate-check/nickname")
    public ApiResponse<?> checkDuplicateNickname(@Valid @RequestBody CheckDuplicateNicknameRequestDTO checkDuplicateNicknameRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.checkNickname(checkDuplicateNicknameRequestDTO.nickname());
        return ApiResponse.onSuccess("사용 가능한 닉네임입니다.");
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
