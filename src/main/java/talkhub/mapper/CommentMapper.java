package talkhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import talkhub.dto.CommentDto;
import talkhub.model.Comment;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    Comment fromCommentDto(CommentDto dto);
    @Mapping(target = "topicId", source = "topic.id")
    CommentDto toCommentDto(Comment comment);
}
