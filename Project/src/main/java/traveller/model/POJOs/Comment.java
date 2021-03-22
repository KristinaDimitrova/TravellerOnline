package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Comment {
   private int id;
   private User owner;
   private Post post;
   private String text;
   private LocalDateTime createdAt;
}
