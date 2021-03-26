package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="comments")
public class Comment {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   @ManyToOne
   @JoinColumn(name="owner_id")
   private User owner;
   @Column
   private String text;
   @Column
   private LocalDateTime createdAt;
   @ManyToOne
   @JoinColumn(name="post_id")
   private Post post;
   //TODO
   private List<User> likers;
}
