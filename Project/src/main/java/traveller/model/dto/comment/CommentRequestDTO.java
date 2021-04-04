package traveller.model.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CommentRequestDTO {
    @Size(min = 5, max = 255, message = "Sorry, your comment must not exceed 255 characters")
    @NotBlank(message = "Field must not be blank")
    private String text;
}
