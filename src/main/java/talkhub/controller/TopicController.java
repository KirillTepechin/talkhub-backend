package talkhub.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import talkhub.dto.TopicDto;
import talkhub.model.User;
import talkhub.service.TopicService;

import java.util.List;

@RestController
@RequestMapping("/topic")
@CrossOrigin(origins = "*")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/{id}")
    public TopicDto getTopic(@PathVariable Long id){
        return topicService.getTopic(id);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public TopicDto createTopic(@RequestBody TopicDto dto, @AuthenticationPrincipal User user){
        return topicService.createTopic(dto, user);
    }

    @GetMapping("/by/comments")
    public List<TopicDto> findAllByCommentsCount(@RequestParam(required = false) List<String> categories){
        return topicService.getTopicsByCommentsCount(categories);
    }

    @GetMapping("/by/date")
    public List<TopicDto> findAllByDate(@RequestParam(required = false) List<String> categories){
        return topicService.getTopicsByDate(categories);
    }

    @GetMapping("/search")
    public List<TopicDto> findAllByQuery(@RequestParam String query){
        return topicService.getTopicsByQuery(query);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public void deleteTopic(@PathVariable Long id){
        topicService.deleteTopic(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public TopicDto editTopic(@PathVariable Long id, @RequestBody TopicDto dto){
        return topicService.editTopic(id, dto);
    }

}
