package postman.bottler.complaint.domain;

import java.util.List;
import lombok.Getter;

@Getter
public class Complaints {
    private static final int WARNING_CONDITION_COUNT = 3;

    private List<Complaint> complaints;

    private Complaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public static Complaints from(List<Complaint> complaints) {
        return new Complaints(complaints);
    }

    public void add(Complaint complaint) {
        complaints.forEach(c -> c.validateDuplicateComplaint(complaint.getLetterId(), complaint.getReporterId()));
        complaints.add(complaint);
    }

    public Boolean needsWarningNotification() {
        return complaints.size() == WARNING_CONDITION_COUNT;
    }
}
