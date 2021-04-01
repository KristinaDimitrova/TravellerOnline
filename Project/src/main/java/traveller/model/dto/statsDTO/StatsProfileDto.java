package traveller.model.dto.statsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.pojo.StatsProfile;

@Setter
@Getter
@NoArgsConstructor
public class StatsProfileDto {
    private int subscribers; //oбщо ИЛИ по някакъв критерии
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private int age;

    public StatsProfileDto(StatsProfile profile) {
        this.firstName = profile.getUser().getFirstName();
        this.lastName = profile.getUser().getLastName();
        this.email = profile.getUser().getEmail();
        this.username = profile.getUser().getUsername();
        this.age = profile.getUser().getAge();
        subscribers = profile.getSubscribers();
    }
}
