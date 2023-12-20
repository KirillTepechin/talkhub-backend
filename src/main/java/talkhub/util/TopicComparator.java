package talkhub.util;

import org.springframework.stereotype.Component;
import talkhub.model.Topic;

import java.util.Comparator;
@Component
public class TopicComparator implements Comparator<Topic> {
    @Override
    public int compare(Topic topic1, Topic topic2) {
        int numberOfComments1 = topic1.getTotalCommentsCount();
        int numberOfComments2 = topic2.getTotalCommentsCount();

        return Integer.compare(numberOfComments2, numberOfComments1);
    }
}