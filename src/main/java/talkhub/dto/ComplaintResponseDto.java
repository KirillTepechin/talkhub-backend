package talkhub.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComplaintResponseDto {
    private TopicDto topic;
    private CommentDto comment;
    private List<UserDto> complaints;
    private ComplaintType complaintType;
}
