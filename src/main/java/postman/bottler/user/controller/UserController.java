package postman.bottler.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.request.ChangePasswordRequestDTO;
import postman.bottler.user.dto.request.CheckDuplicateEmailRequestDTO;
import postman.bottler.user.dto.request.CheckDuplicateNicknameRequestDTO;
import postman.bottler.user.dto.request.CheckPasswordRequestDTO;
import postman.bottler.user.dto.request.NicknameRequestDTO;
import postman.bottler.user.dto.request.ProfileImgRequestDTO;
import postman.bottler.user.dto.request.SignUpRequestDTO;
import postman.bottler.user.dto.response.UserResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.ProfileImageException;
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

    @DeleteMapping
    public ApiResponse<?> deleteUser(@Valid @RequestBody CheckPasswordRequestDTO checkPasswordRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        validateRequestDTO(bindingResult);
        userService.deleteUser(checkPasswordRequestDTO.password(), userDetails.getUsername());
        return ApiResponse.onDeleteSuccess("성공적으로 탈퇴되었습니다.");
    }

    @GetMapping
    public ApiResponse<UserResponseDTO> findUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDTO userResponseDTO = userService.findUser(userDetails.getUsername());
        return ApiResponse.onSuccess(userResponseDTO);
    }

    @PatchMapping("/nickname")
    public ApiResponse<?> updateNickname(@Valid @RequestBody NicknameRequestDTO nicknameRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        validateRequestDTO(bindingResult);
        userService.updateNickname(nicknameRequestDTO.nickname(), userDetails.getUsername());
        return ApiResponse.onSuccess("닉네임이 수정되었습니다.");
    }

    @PatchMapping("/password")
    public ApiResponse<?> updatePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        validateRequestDTO(bindingResult);
        userService.updatePassword(changePasswordRequestDTO.existingPassword(), changePasswordRequestDTO.newPassword(), userDetails.getUsername());
        return ApiResponse.onSuccess("비밀번호가 수정되었습니다.");
    }

    @PatchMapping("/profileImg")
    public ApiResponse<?> updateProfileImage(@Valid @RequestBody ProfileImgRequestDTO profileImgRequestDTO, BindingResult bindingResult, @AuthenticationPrincipal UserDetails userDetails) {
        validateRequestDTO(bindingResult);
        userService.updateProfileImage(profileImgRequestDTO.imageUrl(), userDetails.getUsername());
        return ApiResponse.onSuccess("프로필 이미지가 수정되었습니다.");
    }

    @PostMapping("/profileImg")
    public ApiResponse<?> createProfileImg(@Valid @RequestBody ProfileImgRequestDTO profileImgRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.createProfileImg(profileImgRequestDTO.imageUrl());
        return ApiResponse.onCreateSuccess("프로필 이미지 DB 저장 성공");
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "email" -> throw new EmailException(error.getDefaultMessage());
                    case "password", "existingPassword", "newPassword" -> throw new PasswordException(error.getDefaultMessage());
                    case "nickname" -> throw new NicknameException(error.getDefaultMessage());
                    case "imageUrl" -> throw new ProfileImageException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }
}
