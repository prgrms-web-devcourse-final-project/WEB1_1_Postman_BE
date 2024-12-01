package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.Paper;

import java.util.List;

@Repository
public interface PaperRepository {

    List<Paper> findAll();
}
