package traveller.model.dao.statistics;

import traveller.model.pojo.StatsProfile;

import java.sql.SQLException;
import java.util.List;

public interface StatsDao {
    void saveRecordOfMostPopularPosts();
    void saveRecordOfMostPopularUsers();

    void saveRecordSignups();
    List<StatsProfile> getFavouriteProfilesByAgeGroup(int minRange, int maxRange) throws SQLException;
}
