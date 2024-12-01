package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO letterReplyRequestDTO) {
        ReceiverDTO receiverInfo = letterService.getReceiverInfoById(letterId);
        String title = generateReplyTitle(receiverInfo.title());
        Long receiverId = receiverInfo.receiverId();

        Long senderId = getCurrentUserId();
        String userProfile = getCurrentUserProfile();

        ReplyLetter replyLetter = replyLetterRepository.save(
                letterReplyRequestDTO.toDomain(title, letterId, receiverId, senderId, userProfile)
        );
        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterHeadersResponseDTO> getReplyLetterHeaders(int page, int size, String sort) {
        Long receiverId = getCurrentUserId();

        Pageable pageable = createPageable(page, size, sort);
        return replyLetterRepository.findAll(receiverId, pageable)
                .map(ReplyLetterHeadersResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterHeadersResponseDTO> getReplyLetterHeadersById(
            Long letterId, int page, int size, String sort
    ) {
        Long receiverId = getCurrentUserId();

        Pageable pageable = createPageable(page, size, sort);
        return replyLetterRepository.findAllByLetterId(letterId, receiverId, pageable)
                .map(ReplyLetterHeadersResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ReplyLetterResponseDTO getReplyLetterDetail(Long replyLetterId) {
        ReplyLetter replyLetter = replyLetterRepository.findById(replyLetterId)
                .orElseThrow(() -> new LetterNotFoundException("답장 편지가 존재하지 않습니다."));
        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional
    public void deleteReplyLetter(Long replyLetterId) {
        replyLetterRepository.remove(replyLetterId);
    }

    private String generateReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    private static PageRequest createPageable(int page, int size, String sort) {
        return PageRequest.of(page - 1, size, Sort.by(sort).descending());
    }

    private Long getCurrentUserId() {
        return 1L;
    }

    private String getCurrentUserProfile() {
        return "url";
    }
}
