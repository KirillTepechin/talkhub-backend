package talkhub.util;

import org.springframework.stereotype.Component;
import talkhub.dto.ComplaintResponseDto;

import java.util.Comparator;
@Component
public class ComplaintComparator implements Comparator<ComplaintResponseDto> {
    @Override
    public int compare(ComplaintResponseDto complaint1, ComplaintResponseDto complaint2) {
        int numberOfComplaints1 = complaint1.getComplaints().size();
        int numberOfComplaints2 = complaint2.getComplaints().size();

        return Integer.compare(numberOfComplaints2, numberOfComplaints1);
    }
}