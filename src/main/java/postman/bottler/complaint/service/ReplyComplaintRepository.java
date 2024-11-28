package postman.bottler.complaint.service;

import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.ReplyComplaint;

public interface ReplyComplaintRepository {
    ReplyComplaint save(ReplyComplaint complaint);

    Complaints findByLetterId(Long letterId);
}
