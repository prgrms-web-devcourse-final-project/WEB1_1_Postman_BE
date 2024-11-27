package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;

@Service
@RequiredArgsConstructor
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;
    private final LetterService letterService;

    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO letterReplyRequestDTO) {
        Long userId = 1L;
        String userProfile = "profile url";
        // letterId 로 제목 받아와서 RE 형식 적용
        String title = "RE: [" + letterService.getTitleById(letterId) + "]";

        ReplyLetter replyLetter = replyLetterRepository.save(
                letterReplyRequestDTO.toDomain(title, letterId, userId, userProfile));
        return ReplyLetterResponseDTO.from(replyLetter);
    }


    public Page<ReplyLetterHeadersResponseDTO> getReplyLetterHeaders(int page, int size, String sort) {
        Long userId = 1L;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort).descending());
        return replyLetterRepository.findAll(userId, pageable)
                .map(ReplyLetterHeadersResponseDTO::from);
    }

    public ReplyLetterResponseDTO getReplyLetter(Long replyId) {
        return null;
    }

    public void deleteReplyLetter(Long replyId) {

    }

    public Page<LetterHeadersResponseDTO> getReplyLetterHeadersById(Long letterId, int page, int size, String sort) {
        return null;
    }
}
