package postman.bottler.complaint.service;

import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.MapComplaint;

public interface MapComplaintRepository {
    MapComplaint save(MapComplaint complaint);

    Complaints findByLetterId(Long letterId);
}
