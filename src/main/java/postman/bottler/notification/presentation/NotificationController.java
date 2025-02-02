package postman.bottler.notification.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.notification.application.dto.response.UnreadNotificationResponseDTO;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.application.dto.request.NotificationRequestDTO;
import postman.bottler.notification.application.dto.request.SubscriptionRequestDTO;
import postman.bottler.notification.application.dto.request.UnsubscriptionRequestDTO;
import postman.bottler.notification.application.dto.response.NotificationResponseDTO;
import postman.bottler.notification.application.dto.response.SubscriptionResponseDTO;
import postman.bottler.notification.exception.InvalidNotificationRequestException;
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.notification.application.service.SubscriptionService;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "알림 API", description = "로그인 사용자만 가능")
public class NotificationController {
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    @Operation(summary = "알림 생성",
            description = "알림 유형, 알림 대상은 필수, 편지 관련 알림은 편지 ID와 라벨 이미지를 등록합니다.")
    @PostMapping
    public ApiResponse<NotificationResponseDTO> create(
            @Valid @RequestBody NotificationRequestDTO notificationRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidNotificationRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        NotificationResponseDTO response = notificationService.sendNotification(
                NotificationType.from(notificationRequestDTO.notificationType()),
                notificationRequestDTO.receiver(),
                notificationRequestDTO.letterId(), notificationRequestDTO.label());
        return ApiResponse.onCreateSuccess(response);
    }

    @Operation(summary = "알림 조회", description = "사용자의 알림을 조회합니다.")
    @PatchMapping
    public ApiResponse<List<NotificationResponseDTO>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<NotificationResponseDTO> userNotifications = notificationService.getUserNotifications(
                customUserDetails.getUserId());
        return ApiResponse.onSuccess(userNotifications);
    }

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "사용자의 안읽은 알림 개수를 조회합니다.")
    @GetMapping("/unread")
    public ApiResponse<UnreadNotificationResponseDTO> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UnreadNotificationResponseDTO unreadNotificationCount = notificationService.getUnreadNotificationCount(
                customUserDetails.getUserId());
        return ApiResponse.onSuccess(unreadNotificationCount);
    }

    @Operation(summary = "알림 허용", description = "사용자의 기기 토큰을 등록합니다.")
    @PostMapping("/subscribe")
    public ApiResponse<SubscriptionResponseDTO> subscribe(@RequestBody SubscriptionRequestDTO subscriptionRequest,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        SubscriptionResponseDTO response = subscriptionService.subscribe(
                customUserDetails.getUserId(),
                subscriptionRequest.token());
        return ApiResponse.onCreateSuccess(response);
    }

    @Operation(summary = "전체 알림 비허용", description = "사용자의 알림 기기를 모두 삭제합니다.")
    @DeleteMapping("/subscribe/all")
    public ApiResponse<String> unsubscribeAll(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        subscriptionService.unsubscribeAll(customUserDetails.getUserId());
        return ApiResponse.onDeleteSuccess("삭제 성공");
    }

    @Operation(summary = "기기 알림 비허용", description = "특정 토큰을 삭제합니다.")
    @DeleteMapping("/subscribe")
    public ApiResponse<String> unsubscribe(@RequestBody UnsubscriptionRequestDTO unsubscriptionRequest) {
        subscriptionService.unsubscribe(unsubscriptionRequest.token());
        return ApiResponse.onDeleteSuccess("삭제 성공");
    }
}
