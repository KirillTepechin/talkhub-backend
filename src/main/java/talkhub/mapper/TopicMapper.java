package talkhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import talkhub.dto.TopicDto;
import talkhub.model.Topic;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicMapper {
    Topic fromTopicDto(TopicDto dto);
    @Mapping(target = "commentsCount", expression = "java(topic.getTotalCommentsCount())")
    TopicDto toTopicDto(Topic topic);
}
