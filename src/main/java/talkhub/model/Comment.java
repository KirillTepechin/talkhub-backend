package talkhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String text;
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    @NonNull
    private Topic topic;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment",
            cascade = { CascadeType.REMOVE, CascadeType.PERSIST} )
    private List<Comment> comments;
    @ManyToMany
    @JoinTable(
            name = "user_comment_complaint",
            joinColumns = { @JoinColumn(name = "comment_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> complaints;

}
