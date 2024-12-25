package postman.bottler.mapletter.application.repository;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.Paper;

import java.util.List;

@Repository
public interface PaperRepository {

    List<Paper> findAll();
}
