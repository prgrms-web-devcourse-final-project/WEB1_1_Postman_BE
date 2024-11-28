package postman.bottler.complaint.domain;

import java.util.List;
import postman.bottler.complaint.exception.DuplicateComplainException;

public class Complaints {
    private List<Complaint> complaints;

    private Complaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public static Complaints from(List<Complaint> complaints) {
        return new Complaints(complaints);
    }

    public void validateDuplication(Long reporterId) {
        for (Complaint complaint : complaints) {
            if (complaint.isReporter(reporterId)) {
                throw new DuplicateComplainException();
            }
        }
    }

    public void add(Complaint complaint) {
        complaints.add(complaint);
    }

    public Boolean needsWarningNotification() {
        return complaints.size() == 3;
    }
}
