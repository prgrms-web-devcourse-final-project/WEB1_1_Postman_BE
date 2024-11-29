package postman.bottler.mapletter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.Paper;
import postman.bottler.mapletter.dto.PaperDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final PaperRepository paperRepository;

    @Transactional(readOnly = true)
    public List<PaperDTO> findPapers() {
        List<Paper> papers = paperRepository.findAll();
        return papers.stream()
                .map(Paper::toPaperDTO)
                .toList();
    }
}
