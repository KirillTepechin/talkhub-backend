package talkhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import talkhub.dto.CommentDto;
import talkhub.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment fromCommentDto(CommentDto dto);
    CommentDto toCommentDto(Comment topic);
}
