package talkhub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkhub.dto.ComplaintResponseDto;
import talkhub.dto.ComplaintType;
import talkhub.dto.ComplaintRequestDto;
import talkhub.mapper.ComplaintMapper;
import talkhub.model.Comment;
import talkhub.model.Topic;
import talkhub.model.User;
import talkhub.repository.CommentRepository;
import talkhub.repository.TopicRepository;
import talkhub.util.ComplaintComparator;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ComplaintService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ComplaintMapper complaintMapper;
    @Autowired
    private ComplaintComparator complaintComparator;
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getAllComplaints(){
        List<ComplaintResponseDto> allTopicsByOrderByComplaintsCount = topicRepository.findAllByOrderByComplaintsCount()
                .stream().map(complaintMapper::toComplaintDto).toList();
        List<ComplaintResponseDto> allCommentsByOrderByComplaintsCount = commentRepository.findAllByOrderByComplaintsCount()
                .stream().map(complaintMapper::toComplaintDto).toList();

        List<ComplaintResponseDto> list = new java.util.ArrayList<>(Stream.concat(allTopicsByOrderByComplaintsCount.stream(), allCommentsByOrderByComplaintsCount.stream())
                .toList());

        list.sort(complaintComparator);
        return list;
    }
    @Transactional
    public void createComplaint(ComplaintRequestDto dto, User user){
        if(dto.getComplaintType().equals(ComplaintType.ON_TOPIC)){
            Topic topic = topicRepository.findById(dto.getId()).get();
            topic.getComplaints().add(user);
            topicRepository.save(topic);
        }
        else{
            Comment comment = commentRepository.findById(dto.getId()).get();
            comment.getComplaints().add(user);
            commentRepository.save(comment);
        }
    }

    @Transactional
    public void approveComplaint(ComplaintRequestDto dto) {
        if(dto.getComplaintType().equals(ComplaintType.ON_TOPIC)){
            Topic topic = topicRepository.findById(dto.getId()).get();
            topicRepository.delete(topic);
        }
        else{
            Comment comment = commentRepository.findById(dto.getId()).get();
            commentRepository.delete(comment);
        }
    }

    @Transactional
    public void dismissComplaint(ComplaintRequestDto dto) {
        if(dto.getComplaintType().equals(ComplaintType.ON_TOPIC)){
            Topic topic = topicRepository.findById(dto.getId()).get();
            topic.getComplaints().clear();
            topicRepository.save(topic);
        }
        else{
            Comment comment = commentRepository.findById(dto.getId()).get();
            comment.getComplaints().clear();
            commentRepository.save(comment);
        }
    }
}
