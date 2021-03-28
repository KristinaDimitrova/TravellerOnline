package traveller.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
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

   //comment owner
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
   //People who hit like
   @ManyToMany(mappedBy = "likedComments")
   @JsonManagedReference
   private Set<User> likers;

}
