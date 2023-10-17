package talkhub.dto;

import lombok.Data;

@Data
public class ComplaintRequestDto {
    private ComplaintType complaintType;
    private Long id;
}
