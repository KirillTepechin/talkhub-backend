package talkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkhub.dto.TopicDto;
import talkhub.exception.AccessDeniedException;
import talkhub.exception.NotFoundException;
import talkhub.mapper.TopicMapper;
import talkhub.model.Topic;
import talkhub.model.User;
import talkhub.model.enums.Category;
import talkhub.repository.TopicRepository;
import talkhub.util.CategoryConverter;
import talkhub.util.FileUploadUtil;
import talkhub.util.TopicComparator;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private FileUploadUtil fileUploadUtil;
    @Autowired
    private CategoryConverter categoryConverter;
    @Autowired
    private TopicComparator topicComparator;
    @Transactional(readOnly = true)
    public TopicDto getTopic(Long id){
        return topicMapper.toTopicDto(topicRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicsByCommentsCount(List<Category> categories, boolean allCategoriesIn){
        if (categories == null || categories.isEmpty()) {
            return orderByCommentsCount(topicRepository.findAllByOrderByCommentsCount())
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
        else if(!allCategoriesIn){
            return orderByCommentsCount(topicRepository.findAllByCategoriesInOrderByCommentsCount(categories))
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
        else{
            return orderByCommentsCount(whereCategoriesIn(topicRepository.findAllByOrderByCommentsCount(), categories))
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicsByDate(List<Category> categories, boolean allCategoriesIn){
        if (categories == null || categories.isEmpty()) {
            return topicRepository.findAllByOrderByDateDesc()
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
        else if(!allCategoriesIn){

            return topicRepository.findAllByCategoriesInOrderByDateDesc(categories)
                    .stream().map(topic -> topicMapper.toTopicDto(topic)).toList();
        }
        else {
            return whereCategoriesIn(topicRepository.findAllByOrderByDateDesc(), categories)
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
        if(dto.getBinaryAttachments().length!=0){
            topic.setAttachments(fileUploadUtil.uploadFiles(dto.getBinaryAttachments(), topicRepository.save(topic).getId()));
        }
        return topicMapper.toTopicDto(topicRepository.save(topic));
    }

    @Transactional
    public TopicDto editTopic(Long id, TopicDto dto, User user) {
        if(!dto.getUser().getLogin().equals(user.getLogin())){
            throw new AccessDeniedException();
        }
        Topic topic = topicRepository.findById(id).get();
        if(dto.getName()!=null && !dto.getName().isBlank()){
            topic.setName(dto.getName());
        }
        if(dto.getText()!=null && !dto.getText().isBlank()){
            topic.setText(dto.getText());
        }
        if(topic.getAttachments()!=null){
            fileUploadUtil.deleteDir(topic.getAttachments());
        }
        if(dto.getBinaryAttachments().length!=0){
            topic.setAttachments(fileUploadUtil.uploadFiles(dto.getBinaryAttachments(), topicRepository.save(topic).getId()));
        }
        else {
            topic.setAttachments(null);
        }
        topic.setCategories(dto.getCategories());

        return topicMapper.toTopicDto(topicRepository.save(topic));
    }

    @Transactional
    public void deleteTopic(Long id){
        topicRepository.deleteById(id);
    }

    private List<Topic> whereCategoriesIn(List<Topic> topics, List<Category> categories){
        return topics.stream()
                .filter(topic -> new HashSet<>(topic.getCategories()).containsAll(categories))
                .toList();
    }
    private List<Topic> orderByCommentsCount(List<Topic> topics){
        return topics.stream().sorted(topicComparator).toList();
    }
}
