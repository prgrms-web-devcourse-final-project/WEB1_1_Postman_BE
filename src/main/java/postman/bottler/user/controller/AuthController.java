package postman.bottler.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.request.SignInRequestDTO;
import postman.bottler.user.dto.request.RefreshTokenRequestDTO;
import postman.bottler.user.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.dto.response.SignInResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.TokenException;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signin")
    public ApiResponse<SignInResponseDTO> signin(@Valid @RequestBody SignInRequestDTO signInRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        SignInResponseDTO signInResponseDTO = userService.signin(signInRequestDTO.email(), signInRequestDTO.password());
        return ApiResponse.onSuccess(signInResponseDTO);
    }

    @PostMapping("/validate")
    public ApiResponse<AccessTokenResponseDTO> validateRefreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        AccessTokenResponseDTO newAccessToken = userService.validateRefreshToken(refreshTokenRequestDTO.refreshToken());
        return ApiResponse.onSuccess(newAccessToken);
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "email" -> throw new EmailException(error.getDefaultMessage());
                    case "password" -> throw new PasswordException(error.getDefaultMessage());
                    case "refreshToken" -> throw new TokenException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }
}
