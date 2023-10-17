package talkhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class CommentDto {
    private Long id;
    private String text;
    @JsonFormat(pattern="dd MMM yyyy в HH:mm")
    private Date date;
    private UserDto user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long topicId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long parentCommentId;
    private List<CommentDto> comments;
}
