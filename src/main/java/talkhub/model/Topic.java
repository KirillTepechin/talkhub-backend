package talkhub.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import talkhub.model.enums.Category;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Data
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    private Date date;
    @NonNull
    @Column(length = 5000)
    private String text;
    private String attachments;
    @NonNull
    @ElementCollection(targetClass = Category.class)
    private List<Category> categories;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "topic_id")
    @Where(clause = "parent_comment_id IS NULL")
    @OrderBy("date")
    private List<Comment> comments;
    @ManyToMany
    @JoinTable(
            name = "user_topic_complaint",
            joinColumns = { @JoinColumn(name = "topic_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> complaints;

    public int getTotalCommentsCount() {
        if(comments!=null){
            int count = comments.size();

            for (Comment comment : comments) {
                count += comment.getTotalCommentCount();
            }

            return count;
        }
        return 0;
    }
}
