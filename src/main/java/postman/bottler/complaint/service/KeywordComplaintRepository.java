package postman.bottler.complaint.service;

import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordComplaint;

public interface KeywordComplaintRepository {
    KeywordComplaint save(KeywordComplaint complaint);

    Complaints findByLetterId(Long letterId);
}
