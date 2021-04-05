package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Setter
@Getter
@Component
public class SignUpUserResponseDTO {
    private int id;
    private int age;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

}

