package talkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkhub.dto.TopicDto;
import talkhub.mapper.TopicMapper;
import talkhub.model.Comment;
import talkhub.model.Topic;
import talkhub.model.User;
import talkhub.model.enums.Category;
import talkhub.repository.TopicRepository;
import talkhub.util.FileUploadUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private FileUploadUtil fileUploadUtil;
    @Transactional(readOnly = true)
    public TopicDto getTopic(Long id){
        return topicMapper.toTopicDto(topicRepository.findById(id).get());
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicsByCommentsCount(List<String> categories){
        if (categories == null || categories.isEmpty()) {
            return topicRepository.findAllByOrderByCommentsCount()
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
        else {
            List<Category> enumCategories = categories.stream().map(Category::valueOf).toList();
            return topicRepository.findAllByCategoriesInOrderByCommentsCount(enumCategories)
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicsByDate(List<String> categories){
        if (categories == null || categories.isEmpty()) {
            return topicRepository.findAllByOrderByDateDesc()
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
        else {
            List<Category> enumCategories = categories.stream().map(Category::valueOf).toList();
            return topicRepository.findAllByCategoriesInOrderByDateDesc(enumCategories)
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicsByQuery(String query){
        query = query.toUpperCase();
        return topicRepository.findByNameIgnoreCaseContainingOrTextIgnoreCaseContaining(query, query)
                .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
    }

    @Transactional
    public TopicDto createTopic(TopicDto dto, User user){
        Topic topic = topicMapper.fromTopicDto(dto);
        topic.setDate(new Date());
        topic.setUser(user);
        if(dto.getBinaryAttachments()!=null){
            topic.setAttachments(fileUploadUtil.uploadFiles(dto.getBinaryAttachments(), topicRepository.save(topic).getId()));
        }
        return topicMapper.toTopicDto(topicRepository.save(topic));
    }

    @Transactional
    public TopicDto editTopic(Long id, TopicDto dto) {
        Topic topic = topicRepository.findById(id).get();
        if(dto.getName()!=null && !dto.getName().isBlank()){
            topic.setName(dto.getName());
        }
        if(dto.getText()!=null && !dto.getText().isBlank()){
            topic.setName(dto.getText());
        }
        fileUploadUtil.deleteDir(topic.getAttachments());
        topic.setAttachments(fileUploadUtil.uploadFiles(dto.getBinaryAttachments(), topicRepository.save(topic).getId()));
        topic.setCategories(dto.getCategories());

        return topicMapper.toTopicDto(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopic(Long id){
        topicRepository.deleteById(id);
    }
}
