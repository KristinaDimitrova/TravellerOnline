package traveller.model.dto.commentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CommentRequestDto {
    @Size(max = 255, message = "Sorry, your comment must not exceed 255 characters")
    @NotBlank(message = "Field must not be blank")
    private String text;
}
