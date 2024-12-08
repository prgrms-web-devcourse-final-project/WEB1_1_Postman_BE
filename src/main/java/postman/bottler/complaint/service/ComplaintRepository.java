package postman.bottler.complaint.service;

import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.domain.Complaints;

public interface ComplaintRepository {
    Complaint save(Complaint complaint);

    Complaints findByLetterId(Long letterId);
}
