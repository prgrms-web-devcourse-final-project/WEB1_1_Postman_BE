package postman.bottler.complaint.service;

import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordReplyComplaint;

public interface KeywordReplyComplaintRepository {
    KeywordReplyComplaint save(KeywordReplyComplaint complaint);

    Complaints findByLetterId(Long letterId);
}
