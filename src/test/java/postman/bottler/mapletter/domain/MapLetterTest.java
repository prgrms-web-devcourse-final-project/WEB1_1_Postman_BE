package postman.bottler.mapletter.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;

class MapLetterTest {

    @Test
    @DisplayName("퍼블릭 편지 생성에 성공한다")
    void createPublicMapLetterTest() {
        //given
        CreatePublicMapLetterRequestDTO requestDTO = new CreatePublicMapLetterRequestDTO(
                "퍼블릭 편지",
                "편지 내용",
                "역곡역 씨지비 앞",
                new BigDecimal("37.5665"),
                new BigDecimal("127.9780"),
                "프리텐다드",
                "www.paper.com",
                "www.label.com"
        );
        Long userId = 1L;

        //when
        MapLetter mapLetter = MapLetter.createPublicMapLetter(requestDTO, userId);

        //then
        assertNotNull(mapLetter);
        assertEquals("퍼블릭 편지", mapLetter.getTitle());
        assertEquals("편지 내용", mapLetter.getContent());
        assertEquals("역곡역 씨지비 앞", mapLetter.getDescription());
        assertEquals(MapLetterType.PUBLIC, mapLetter.getType());
        assertEquals(userId, mapLetter.getCreateUserId());
        assertFalse(mapLetter.isDeleted());
        assertFalse(mapLetter.isBlocked());
    }

    @Test
    @DisplayName("타겟 편지 생성에 성공한다")
    void createTargetMapLetterTest() {
        //given
        String targetUser = "타겟";
        Long targetUserId=2L;

        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
                "타겟 편지",
                "편지 내용",
                "우리 첫 만남 장소",
                new BigDecimal("37.5665"),
                new BigDecimal("127.9780"),
                "프리텐다드",
                "www.paper.com",
                "www.label.com",
                targetUser
        );
        Long userId = 1L;

        //when
        MapLetter mapLetter = MapLetter.createTargetMapLetter(requestDTO, userId, targetUserId);

        //then
        assertNotNull(mapLetter);
        assertEquals("타겟 편지", mapLetter.getTitle());
        assertEquals("편지 내용", mapLetter.getContent());
        assertEquals("우리 첫 만남 장소", mapLetter.getDescription());
        assertEquals(MapLetterType.PRIVATE, mapLetter.getType());
        assertEquals(userId, mapLetter.getCreateUserId());
        assertEquals(targetUserId, mapLetter.getTargetUserId());
        assertFalse(mapLetter.isDeleted());
        assertFalse(mapLetter.isBlocked());
    }
}
