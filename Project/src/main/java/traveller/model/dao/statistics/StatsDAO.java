package traveller.model.dao.statistics;

import traveller.model.pojo.stats.StatsSignups;
import traveller.model.pojo.stats.StatsProfile;

import java.sql.SQLException;
import java.util.List;

public interface StatsDAO {
    void saveRecordOfMostPopularPosts();
    void saveRecordOfMostPopularUsers();

    void saveRecordSignups();
    List<StatsProfile> getFavouriteProfilesByAgeGroup(int minRange, int maxRange) throws SQLException;

    StatsSignups getSignupsCountByAgeRange(int minAge, int maxAge, int periodDays);
}
