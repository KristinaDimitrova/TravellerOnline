package traveller.model.dto.commentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentCreationRequestDto {
    private String text;
    private long postId;
}
