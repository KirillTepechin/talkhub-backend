package talkhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import talkhub.dto.ComplaintResponseDto;
import talkhub.dto.ComplaintType;
import talkhub.dto.UserDto;
import talkhub.model.Comment;
import talkhub.model.Topic;
import talkhub.model.User;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class, TopicMapper.class})
public interface ComplaintMapper {
    TopicMapper topicMapper = Mappers.getMapper(TopicMapper.class);
    CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
    default ComplaintResponseDto toComplaintDto(Topic topic){
        ComplaintResponseDto complaintDto = new ComplaintResponseDto();
        complaintDto.setTopic(topicMapper.toTopicDto(topic));
        complaintDto.setComplaints(mapComplaints(topic.getComplaints()));
        complaintDto.setComplaintType(ComplaintType.ON_TOPIC);
        return  complaintDto;
    }

    default ComplaintResponseDto toComplaintDto(Comment comment){
        ComplaintResponseDto complaintDto = new ComplaintResponseDto();
        complaintDto.setComment(commentMapper.toCommentDto(comment));
        complaintDto.setComplaints(mapComplaints(comment.getComplaints()));
        complaintDto.setComplaintType(ComplaintType.ON_COMMENT);
        return  complaintDto;
    }

    List<UserDto> mapComplaints(Set<User> complaints);
}
