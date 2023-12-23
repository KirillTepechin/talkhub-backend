package talkhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import talkhub.dto.CommentDto;
import talkhub.model.Comment;
import talkhub.model.Topic;
import talkhub.model.User;
import talkhub.model.enums.Category;
import talkhub.model.enums.Role;
import talkhub.repository.CommentRepository;
import talkhub.repository.TopicRepository;
import talkhub.repository.UserRepository;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Comment createTestData(){
        User user = new User("user", "password", Role.USER);
        userRepository.save(user);

        Topic topic = new Topic();
        topic.setText("topic");
        topic.setName("topic");
        topic.setDate(new Date());
        topic.setUser(userRepository.findByLogin("user"));
        topic.setCategories(List.of(Category.IT));

        topic = topicRepository.save(topic);

        Comment comment = new Comment();
        comment.setText("comment");
        comment.setDate(new Date());
        comment.setUser(userRepository.findByLogin("user"));
        comment.setTopic(topic);

        return commentRepository.save(comment);
    }

    @AfterEach
    public void reset(){
        commentRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getCommentTest() throws Exception {
        long id = createTestData().getId();

        mockMvc.perform(
                        get("/comment/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.text").value("comment"));
    }

    @Test
    @WithMockUser
    public void createCommentTest() throws Exception {
        Long topicId = createTestData().getTopic().getId();

        CommentDto commentDto = new CommentDto();
        commentDto.setText("post comment");
        commentDto.setTopicId(topicId);

        mockMvc.perform(
                        post("/comment")
                                .content(objectMapper.writeValueAsString(commentDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("post comment"));
    }


    @Test
    @WithMockUser
    public void editCommentTest() throws Exception {
        Long commentId = createTestData().getId();

        CommentDto commentDto = new CommentDto();
        commentDto.setText("edited comment");
        commentDto.setTopicId(1L);


        mockMvc.perform(put("/comment/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.text").value("edited comment"));

    }

}
