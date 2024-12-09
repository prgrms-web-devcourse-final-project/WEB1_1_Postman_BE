package postman.bottler.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.auth.CustomUserDetails;
import postman.bottler.user.dto.request.AuthEmailRequestDTO;
import postman.bottler.user.dto.request.ChangePasswordRequestDTO;
import postman.bottler.user.dto.request.CheckDuplicateNicknameRequestDTO;
import postman.bottler.user.dto.request.CheckPasswordRequestDTO;
import postman.bottler.user.dto.request.EmailRequestDTO;
import postman.bottler.user.dto.request.NicknameRequestDTO;
import postman.bottler.user.dto.request.ProfileImgRequestDTO;
import postman.bottler.user.dto.request.SignUpRequestDTO;
import postman.bottler.user.dto.response.ExistingUserResponseDTO;
import postman.bottler.user.dto.response.UserResponseDTO;
import postman.bottler.user.exception.EmailCodeException;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.ProfileImageException;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "유저", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "이메일, 비밀번호, 닉네임으로 회원가입을 합니다.")
    @PostMapping("/signup")
    public ApiResponse<String> signup(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.createUser(signUpRequestDTO.email(), signUpRequestDTO.password(), signUpRequestDTO.nickname());
        return ApiResponse.onCreateSuccess("회원가입 성공");
    }

    @Operation(summary = "이메일 중복 확인", description = "입력된 이메일이 이미 등록된 이메일인지 확인합니다.")
    @PostMapping("/duplicate-check/email")
    public ApiResponse<String> checkDuplicateEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO,
                                              BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.checkEmail(emailRequestDTO.email());
        return ApiResponse.onSuccess("사용 가능한 이메일입니다.");
    }

    @Operation(summary = "닉네임 중복 확인", description = "입력된 닉네임이 이미 등록된 닉네임인지 확인합니다.")
    @PostMapping("/duplicate-check/nickname")
    public ApiResponse<String> checkDuplicateNickname(
            @Valid @RequestBody CheckDuplicateNicknameRequestDTO checkDuplicateNicknameRequestDTO,
            BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.checkNickname(checkDuplicateNicknameRequestDTO.nickname());
        return ApiResponse.onSuccess("사용 가능한 닉네임입니다.");
    }

    @Operation(summary = "유저 탈퇴", description = "(로그인 필요) 비밀번호를 확인 후 탈퇴를 진행합니다.")
    @DeleteMapping
    public ApiResponse<String> deleteUser(@Valid @RequestBody CheckPasswordRequestDTO checkPasswordRequestDTO,
                                     BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        validateRequestDTO(bindingResult);
        userService.deleteUser(checkPasswordRequestDTO.password(), customUserDetails.getEmail());
        return ApiResponse.onDeleteSuccess("성공적으로 탈퇴되었습니다.");
    }

    @Operation(summary = "유저 정보 조회", description = "(로그인 필요) 로그인한 유저의 정보를 조회합니다.")
    @GetMapping
    public ApiResponse<UserResponseDTO> findUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserResponseDTO userResponseDTO = userService.findUser(customUserDetails.getEmail());
        return ApiResponse.onSuccess(userResponseDTO);
    }

    @Operation(summary = "닉네임 수정", description = "(로그인 필요) 입력된 닉네임으로 수정합니다.")
    @PatchMapping("/nickname")
    public ApiResponse<String> updateNickname(@Valid @RequestBody NicknameRequestDTO nicknameRequestDTO,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        validateRequestDTO(bindingResult);
        userService.updateNickname(nicknameRequestDTO.nickname(), customUserDetails.getEmail());
        return ApiResponse.onSuccess("닉네임이 수정되었습니다.");
    }

    @Operation(summary = "비밀번호 수정", description = "(로그인 필요) 입력된 비밀번호를 확인 후 수정합니다.")
    @PatchMapping("/password")
    public ApiResponse<String> updatePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        validateRequestDTO(bindingResult);
        userService.updatePassword(changePasswordRequestDTO.existingPassword(), changePasswordRequestDTO.newPassword(),
                customUserDetails.getEmail());
        return ApiResponse.onSuccess("비밀번호가 수정되었습니다.");
    }

    @Operation(summary = "프로필 이미지 수정", description = "(로그인 필요) 입력된 프로필 이미지 URL을 확인 후 수정합니다.")
    @PatchMapping("/profileImg")
    public ApiResponse<String> updateProfileImage(@Valid @RequestBody ProfileImgRequestDTO profileImgRequestDTO,
                                             BindingResult bindingResult,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        validateRequestDTO(bindingResult);
        userService.updateProfileImage(profileImgRequestDTO.imageUrl(), customUserDetails.getEmail());
        return ApiResponse.onSuccess("프로필 이미지가 수정되었습니다.");
    }

    @Operation(summary = "프로필 이미지 생성", description = "프로필 이미지 URL을 DB에 저장합니다. 실제 서비스에서 사용하는 API는 아닙니다!")
    @PostMapping("/profileImg")
    public ApiResponse<String> createProfileImg(@Valid @RequestBody ProfileImgRequestDTO profileImgRequestDTO,
                                           BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.createProfileImg(profileImgRequestDTO.imageUrl());
        return ApiResponse.onCreateSuccess("프로필 이미지 DB 저장 성공");
    }

    @Operation(summary = "닉네임으로 사용자 존재 확인", description = "(로그인 필요) 입력된 닉네임에 해당하는 사용자가 있는지 확인합니다.")
    @GetMapping("/exists")
    public ApiResponse<ExistingUserResponseDTO> findUser(@RequestParam String nickname) {
        ExistingUserResponseDTO existingUserResponseDTO = userService.findExistingUser(nickname);
        return ApiResponse.onSuccess(existingUserResponseDTO);
    }

    @Operation(summary = "이메일 인증 코드 요청", description = "입력된 이메일로 인증 코드 요청 이메일을 보냅니다.")
    @PostMapping("/email/send")
    public ApiResponse<String> sendEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.sendCodeToEmail(emailRequestDTO.email());
        return ApiResponse.onSuccess("이메일 인증 요청을 성공했습니다.");
    }

    @Operation(summary = "이메일 인증 코드 검사", description = "인증 코드를 검사합니다.")
    @PostMapping("/email/verify")
    public ApiResponse<String> verifyEmail(@Valid @RequestBody AuthEmailRequestDTO authEmailRequestDTO) {
        userService.verifyCode(authEmailRequestDTO.email(), authEmailRequestDTO.code());
        return ApiResponse.onSuccess("이메일 인증을 성공했습니다.");
    }

    @Operation(summary = "개발자 계정 생성", description = "개발자 계정 생성")
    @PostMapping("/developer")
    public ApiResponse<String> createDeveloper(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        userService.createDeveloper(signUpRequestDTO.email(), signUpRequestDTO.password(), signUpRequestDTO.nickname());
        return ApiResponse.onCreateSuccess("개발자 회원가입 성공");
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "email" -> throw new EmailException(error.getDefaultMessage());
                    case "password", "existingPassword", "newPassword" ->
                            throw new PasswordException(error.getDefaultMessage());
                    case "nickname" -> throw new NicknameException(error.getDefaultMessage());
                    case "imageUrl" -> throw new ProfileImageException(error.getDefaultMessage());
                    case "code" -> throw new EmailCodeException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }
}
