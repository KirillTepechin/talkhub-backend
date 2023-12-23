package talkhub;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import talkhub.dto.TopicDto;
import talkhub.mapper.TopicMapper;
import talkhub.model.Topic;
import talkhub.model.User;
import talkhub.model.enums.Category;
import talkhub.model.enums.Role;
import talkhub.repository.CommentRepository;
import talkhub.repository.TopicRepository;
import talkhub.repository.UserRepository;
import talkhub.service.TopicService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TopicServiceTests {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicService topicService;


    @AfterEach
    public void reset(){
        commentRepository.deleteAll();
        topicRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void generateData(){
        User user = new User("user", "password", Role.USER);
        userRepository.save(user);


        Topic topic1 = new Topic();
        topic1.setText("text1");
        topic1.setName("topic");
        Date date = new Date();
        date.setTime(1);
        topic1.setDate(date);
        topic1.setUser(userRepository.findByLogin("user"));
        topic1.setCategories(List.of(Category.IT));
        topic1.setComments(new ArrayList<>());

        topicRepository.save(topic1);

        Topic topic2 = new Topic();
        topic2.setText("text2");
        topic2.setName("topic");
        topic2.setDate(new Date());
        topic2.setUser(userRepository.findByLogin("user"));
        topic2.setCategories(List.of(Category.IT));
        topic2.setComments(new ArrayList<>());
        topicRepository.save(topic2);
    }

    @Test
    public void testGetTopicsByDate() {
        generateData();

        List<Topic> all = topicRepository.findAll();
        Topic topic1 = all.get(0);
        Topic topic2 = all.get(1);


        List<TopicDto> expected = List.of(topicMapper.toTopicDto(topic2), topicMapper.toTopicDto(topic1));

        List<TopicDto> result = topicService.getTopicsByDate(null, false);

        assertEquals(expected, result);
    }

    @Test
    public void testGetTopicsByQuery() {
        generateData();

        List<Topic> all = topicRepository.findAll();
        Topic topic1 = all.get(0);


        List<TopicDto> expected = List.of(topicMapper.toTopicDto(topic1));

        List<TopicDto> result = topicService.getTopicsByQuery("text1");

        assertEquals(expected, result);
    }
}
