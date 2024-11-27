package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;

@Service
@RequiredArgsConstructor
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;

    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO letterReplyRequestDTO) {
        return null;
    }

    public Page<LetterHeadersResponseDTO> getReplyLetterHeaders(int page, int size, String sort) {
        return null;
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
