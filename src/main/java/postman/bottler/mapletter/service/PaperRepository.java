package postman.bottler.mapletter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.Paper;
import postman.bottler.mapletter.dto.PaperDTO;

import java.util.List;

@Repository
public interface PaperRepository {

    List<Paper> findAll();
}
