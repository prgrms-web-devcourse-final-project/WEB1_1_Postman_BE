package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.dto.ReceiverDTO;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.ReplyLetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.exception.LetterNotFoundException;

@Service
@RequiredArgsConstructor
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;
    private final LetterService letterService;

    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO letterReplyRequestDTO) {
        ReceiverDTO receiverInfo = letterService.getReceiverInfoById(letterId);
        // letterId 로 제목 받아와서 RE 형식 적용
        String title = generateReplyTitle(receiverInfo.title());
        Long receiverId = receiverInfo.receiverId();
        Long senderId = 2L;
        String userProfile = "profile url";

        ReplyLetter replyLetter = replyLetterRepository.save(
                letterReplyRequestDTO.toDomain(title, letterId, receiverId, senderId, userProfile));
        return ReplyLetterResponseDTO.from(replyLetter);
    }


    public Page<ReplyLetterHeadersResponseDTO> getReplyLetterHeaders(int page, int size, String sort) {
        Long userId = 1L;

        Pageable pageable = createPageable(page, size, sort);
        return replyLetterRepository.findAll(userId, pageable)
                .map(ReplyLetterHeadersResponseDTO::from);
    }

    public Page<ReplyLetterHeadersResponseDTO> getReplyLetterHeadersById(
            Long letterId, int page, int size, String sort
    ) {
        Long userId = 1L;

        Pageable pageable = createPageable(page, size, sort);
        return replyLetterRepository.findAllByLetterId(letterId, userId, pageable)
                .map(ReplyLetterHeadersResponseDTO::from);
    }

    public ReplyLetterResponseDTO getReplyLetterDetail(Long replyLetterId) {
        ReplyLetter replyLetter = replyLetterRepository.findById(replyLetterId)
                .orElseThrow(() -> new LetterNotFoundException("답장 편지가 존재하지 않습니다."));
        return ReplyLetterResponseDTO.from(replyLetter);
    }

    public void deleteReplyLetter(Long replyLetterId) {
        replyLetterRepository.remove(replyLetterId);
    }

    private String generateReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    private static PageRequest createPageable(int page, int size, String sort) {
        return PageRequest.of(page - 1, size, Sort.by(sort).descending());
    }
}
