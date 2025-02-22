package postman.bottler.mapletter.application.repository;

import java.util.List;

public interface RecentReplyStorage {
    List<Object> getRecentReplies(Long userId);

    void saveRecentReply(Long userId, String type, Long letterId, String labelUrl);

    void deleteRecentReply(Long userId, String type, Long letterId, String labelUrl);
}
