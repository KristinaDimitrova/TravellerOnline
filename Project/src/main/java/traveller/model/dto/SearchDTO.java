package traveller.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@NoArgsConstructor
public class SearchDTO {
    @NotNull
    private String name;
    @NotNull
    private String locationType;

}
