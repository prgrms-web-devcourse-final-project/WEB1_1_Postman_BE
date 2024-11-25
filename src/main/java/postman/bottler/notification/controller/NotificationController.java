package postman.bottler.notification.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.dto.request.NotificationRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;
import postman.bottler.notification.exception.InvalidNotificationRequestException;
import postman.bottler.notification.service.NotificationService;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ApiResponse<?> create(@Valid @RequestBody NotificationRequestDTO notificationRequestDTO,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidNotificationRequestException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        Notification notification = notificationService.sendNotification(notificationRequestDTO.notificationType(),
                notificationRequestDTO.receiver(), notificationRequestDTO.letterId());
        return ApiResponse.onCreateSuccess(NotificationResponseDTO.from(notification));
    }
}
