package postman.bottler.complaint.service;

import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.MapReplyComplaint;

public interface MapReplyComplaintRepository {
    MapReplyComplaint save(MapReplyComplaint complaint);

    Complaints findByLetterId(Long letterId);
}
