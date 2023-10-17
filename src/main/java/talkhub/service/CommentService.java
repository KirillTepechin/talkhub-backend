package talkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkhub.dto.CommentDto;
import talkhub.mapper.CommentMapper;
import talkhub.mapper.TopicMapper;
import talkhub.model.Comment;
import talkhub.model.Topic;
import talkhub.model.User;
import talkhub.repository.CommentRepository;

import java.util.Date;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicService topicService;

    @Transactional(readOnly = true)
    public CommentDto getComment(Long id){
        return  commentMapper.toCommentDto(commentRepository.findById(id).get());
    }

    @Transactional
    public CommentDto createComment(CommentDto commentDto, User user){
        Comment comment = commentMapper.fromCommentDto(commentDto);
        comment.setTopic(topicMapper.fromTopicDto(topicService.getTopic(commentDto.getTopicId())));
        if(commentDto.getParentCommentId()!=null){
            comment.setParentComment(commentRepository.findById(commentDto.getParentCommentId()).get());
        }
        comment.setDate(new Date());
        comment.setUser(user);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public CommentDto editComment(Long id, CommentDto dto) {
        Comment comment = commentRepository.findById(id).get();
        comment.setText(dto.getText());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}
