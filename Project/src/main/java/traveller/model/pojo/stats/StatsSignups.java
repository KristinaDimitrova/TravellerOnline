package traveller.model.pojo.stats;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatsSignups {
    private int minAge;
    private int maxAge;
    private int newAccountsCreated;
    private int periodDays;
}
