package talkhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import talkhub.dto.TopicDto;
import talkhub.model.Topic;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TopicMapper {
    Topic fromTopicDto(TopicDto dto);
    TopicDto toTopicDto(Topic topic);
}
