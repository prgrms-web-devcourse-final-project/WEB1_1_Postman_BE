package postman.bottler.notification.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.notification.dto.request.NotificationRequestDTO;
import postman.bottler.notification.dto.request.SubscriptionRequestDTO;
import postman.bottler.notification.dto.request.UserNotificationRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;
import postman.bottler.notification.dto.response.SubscriptionResponseDTO;
import postman.bottler.notification.exception.InvalidNotificationRequestException;
import postman.bottler.notification.service.NotificationService;
import postman.bottler.notification.service.SubscriptionService;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ApiResponse<?> create(@Valid @RequestBody NotificationRequestDTO notificationRequestDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidNotificationRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        NotificationResponseDTO response = notificationService.sendNotification(
                notificationRequestDTO.notificationType(),
                notificationRequestDTO.receiver(),
                notificationRequestDTO.letterId());
        return ApiResponse.onCreateSuccess(response);
    }

    @GetMapping
    public ApiResponse<?> getNotifications(@RequestBody UserNotificationRequestDTO userNotificationRequestDTO) {
        // TODO 추후 JWT를 통해 사용자 획득
        List<NotificationResponseDTO> userNotifications = notificationService.getUserNotifications(
                userNotificationRequestDTO.userId());
        return ApiResponse.onSuccess(userNotifications);
    }

    @PostMapping("/subscribe")
    public ApiResponse<?> subscribe(@RequestBody SubscriptionRequestDTO subscriptionRequest) {
        SubscriptionResponseDTO response = subscriptionService.subscribe(
                subscriptionRequest.userId(),
                subscriptionRequest.deviceToken());
        return ApiResponse.onCreateSuccess(response);
    }
}
