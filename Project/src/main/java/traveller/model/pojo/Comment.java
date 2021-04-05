package traveller.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.comment.CommentRequestDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="comments")
public class Comment {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   //COMMENT OWNER
   @ManyToOne
   @JoinColumn(name="owner_id")
   private User owner;

   @Column
   private String text;
   @Column
   @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
   private LocalDateTime createdAt;
   //POST
   @ManyToOne
   @JoinColumn(name="post_id")
   @JsonBackReference
   private Post post;
   //PEOPLE WHO LIKE IT
   @ManyToMany(cascade = { CascadeType.ALL })
   @JoinTable(
           name = "users_like_comments",
           joinColumns = {@JoinColumn(name="comment_id")},
           inverseJoinColumns = {@JoinColumn(name="user_id")})
   private Set<User> likers = new HashSet<>();

   public Comment(CommentRequestDTO dto){
      this.text = dto.getText();
      createdAt = LocalDateTime.now();
      likers = new HashSet<>();
   }



}
