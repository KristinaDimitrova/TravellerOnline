package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment {
   @Id
   private int id;
   @Column
   private int ownerId;
   @Column
   private int postId;
   @Column
   private String text;
   @Column
   private LocalDateTime createdAt;
}
