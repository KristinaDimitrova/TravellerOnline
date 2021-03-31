package traveller.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDTO {
    @NotBlank(message = "Field must not be blank.") //todo
    private  String message;
}
