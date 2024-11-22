package postman.bottler.notification.controller;

public interface NotificationService {
    void sendNewLetterNotification(long userId, long letterId);

    void sendTargetLetterNotification(long userId, long letterId);

    void sendReplyLetterNotification(long userId, long letterId);

    void sendWarningNotification(long userId);

    void sendBanNotification(long userId);
}
