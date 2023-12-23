package talkhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import talkhub.dto.CommentDto;
import talkhub.dto.UserCredentialsDto;
import talkhub.model.User;
import talkhub.repository.UserRepository;
import talkhub.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {

    @Autowired
    UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @AfterEach
    public void reset(){
        userRepository.deleteAll();
    }

    @Test
    void loginExceptionTest(){
        User existedUser = userService.findByLogin("user");

        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setLogin("user");
        userCredentialsDto.setPassword("password");
        if(existedUser==null){

            userService.register(userCredentialsDto);
        }
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.register(userCredentialsDto));
    }

    @Test
    public void authPassTest() throws Exception{
        UserCredentialsDto userCredentialsDto = new UserCredentialsDto();
        userCredentialsDto.setLogin("kiraTep");
        userCredentialsDto.setPassword("kiraTep");

        userService.register(userCredentialsDto);

        mockMvc.perform(
                        post("/auth")
                                .content(objectMapper.writeValueAsString(userCredentialsDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString());
    }

    @Test
    public void editCredentialsTest() throws Exception{
        UserCredentialsDto oldUser = new UserCredentialsDto();
        oldUser.setLogin("user");
        oldUser.setPassword("password");

        UserCredentialsDto newUser = new UserCredentialsDto();
        newUser.setLogin("user");
        newUser.setPassword("password");
        newUser.setNewPassword("newPassword");

        userService.register(oldUser);

        mockMvc.perform(
                        put("/edit-credentials")
                                .content(objectMapper.writeValueAsString(newUser))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer "+userService.auth(oldUser))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void editPhotoTest() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg",
                "image/jpeg", "test".getBytes());
        UserCredentialsDto oldUser = new UserCredentialsDto();
        oldUser.setLogin("user");
        oldUser.setPassword("password");

        userService.register(oldUser);

        mockMvc.perform(multipart(HttpMethod.PUT,"/edit-photo")
                        .file(file)
                        .header("Authorization", "Bearer "+userService.auth(oldUser)))
                .andExpect(status().isOk());

    }
}
