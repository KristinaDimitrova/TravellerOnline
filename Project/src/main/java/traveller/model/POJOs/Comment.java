package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="comments")
public class Comment {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;
   @Column
   private int ownerId;
   @Column
   private int postId;
   @Column
   private String text;
   @Column
   private LocalDateTime createdAt;
   @ManyToOne
   @JoinColumn(name="post_id")
   private Post post;
}
