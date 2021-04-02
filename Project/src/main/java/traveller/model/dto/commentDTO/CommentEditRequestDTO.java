package traveller.model.dto.commentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommentEditRequestDTO { // no usages
    private long id;
    private String text;
}
