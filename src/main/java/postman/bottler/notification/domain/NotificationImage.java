package postman.bottler.notification.domain;

import postman.bottler.notification.exception.NeedLabelException;

public enum NotificationImage {
    BAN("https://img.hankyung.com/photo/202403/AA.36104679.1.jpg", NotificationType.BAN),
    WARNING("https://img.hankyung.com/photo/202403/AA.36104679.1.jpg", NotificationType.WARNING);
    private final String image;
    private final NotificationType type;

    NotificationImage(String image, NotificationType type) {
        this.image = image;
        this.type = type;
    }

    public static String getImage(NotificationType type) {
        for (NotificationImage value : NotificationImage.values()) {
            if (value.type == type) {
                return value.image;
            }
        }
        throw new NeedLabelException();
    }
}
