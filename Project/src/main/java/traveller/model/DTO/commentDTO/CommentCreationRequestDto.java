package traveller.model.DTO.commentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentCreationRequestDto {
    private String text;
    private long postId;
    private long ownerId;

    public CommentCreationRequestDto(String text, long postId, long ownerId) {
        this.text = text;
        this.postId = postId;
        this.ownerId = ownerId;
    }
}
