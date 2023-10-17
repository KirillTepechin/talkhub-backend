package talkhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import talkhub.model.enums.Role;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String login;
    private String password;
    private Role role;
    private String photo;
}
