package traveller.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class SearchDTO {
    private String searchBy;
    private String searchByValue;
    private boolean orderedByDateOfCreation;
    private boolean orderedByLikes;
}
