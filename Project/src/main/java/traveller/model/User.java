package traveller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class User { //todo Moni

    private int id;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private String password;
}
