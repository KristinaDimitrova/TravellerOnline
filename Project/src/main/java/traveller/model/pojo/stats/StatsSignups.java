package traveller.model.pojo.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsSignups {
    private int minAge;
    private int maxAge;
    private int newAccountsCreated;
    private int periodDays;
}
