package talkhub.dto;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String login;
    private String password;
    private String newPassword;
}
