package postman.bottler.mapletter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.mapletter.domain.Paper;
import postman.bottler.mapletter.dto.PaperDTO;
import postman.bottler.mapletter.infra.entity.PaperEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaperService {
    private final PaperRepository paperRepository;

    public List<PaperDTO> findPapers(){
        List<Paper> papers=paperRepository.findAll();
        return papers.stream()
                .map(Paper::toPaperDTO)
                .toList();
    }
}
