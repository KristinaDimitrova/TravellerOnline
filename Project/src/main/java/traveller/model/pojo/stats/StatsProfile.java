package traveller.model.pojo.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import traveller.model.pojo.User;

@Getter
@Setter
@AllArgsConstructor
public class StatsProfile {
    @NotNull
    private int subscribers; //POJO was created exactly because of this field -> it's value depends on the SELECT query
    @NotNull
    private User user;

    public StatsProfile(User user, int amountSubscribers){
       this.user = user;
       this.subscribers = amountSubscribers;
    }
}
