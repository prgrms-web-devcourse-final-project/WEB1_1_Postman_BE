package postman.bottler.user.controller;

public interface UserService {
    void blockUser(Long userId);
    String findUserImageUrl(Long userId);
}
