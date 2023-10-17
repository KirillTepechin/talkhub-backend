package talkhub.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import talkhub.model.Comment;
import talkhub.model.User;
import talkhub.model.enums.Category;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class TopicDto {
    private Long id;
    private String name;
    private String text;
    @JsonFormat(pattern="dd MMM yyyy в HH:mm")
    private Date date;
    private String attachments;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private FileDto[] binaryAttachments;
    private List<Category> categories;
    private List<CommentDto> comments;
    private UserDto user;
}
