package talkhub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import talkhub.dto.UserCredentialsDto;
import talkhub.dto.UserDto;
import talkhub.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User fromUserCredentialsDto(UserCredentialsDto dto);
    UserDto toUserDto(User user);
}
