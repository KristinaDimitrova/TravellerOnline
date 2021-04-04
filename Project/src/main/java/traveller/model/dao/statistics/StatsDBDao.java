package traveller.model.dao.statistics;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import traveller.exception.TechnicalIssuesException;
import traveller.model.dao.AbstractDao;
import traveller.model.pojo.stats.StatsSignups;
import traveller.model.pojo.stats.StatsProfile;
import traveller.model.pojo.User;
import traveller.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Log4j2
@Component
public class StatsDBDao extends AbstractDao implements StatsDAO {
    //NB query can execute only if there are at least 5 followed users
    private static final int POP_POSTS_RESULTS = 5;
    private static final int POP_USERS_RESULTS = 5;
    private static final int RECORD_SIGNUPS_INTERVAL_DAYS = 7;
    private static final int RECORD_POP_POSTS_INTERVAL_DAYS = 7;
    private static final String COUNT_NEW_USERS = "new_users_count";
    private static final String POST_ID = "post_id";
    private static final String USER_ID = "user_id";
    private static final String AGE = "age";
    private static final String SUBSCRIBERS = "subscribers";

    @Autowired
    private UserRepository userRepo;

    @Override
    public void saveRecordOfMostPopularPosts(){ //todo periodically
        String sqlQuerySelectTopPosts = "SELECT p.id AS " + POST_ID + ", p.created_at, u.id AS post_owner_id, " +
                "COUNT(ulp.owner_id) AS likes FROM posts AS p\n" +
                "INNER JOIN users AS u\n" +
                "ON(u.id=p.owner_id)\n" +
                "INNER JOIN users_like_posts AS ulp\n" +
                "ON(p.id = ulp.post_id)\n" +
                "GROUP BY p.id\n" +
                "HAVING created_at BETWEEN DATE_SUB(NOW(), INTERVAL " + RECORD_POP_POSTS_INTERVAL_DAYS +" DAY) AND NOW()\n" +
                "ORDER BY likes DESC LIMIT " + POP_POSTS_RESULTS;
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlQuerySelectTopPosts);
            ResultSet rows = ps.executeQuery();
            //System.out.println(sqlQuerySelectTopPosts);
            insertIntoTopPostsTable(rows);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void insertIntoTopPostsTable(ResultSet rows){
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO top_ten_posts (post_id, ranking, date)\n" +
                "VALUES ");
        for(int rank = 1; rank <= POP_POSTS_RESULTS; rank++){
            sqlInsertQuery.append("\n(?, " + rank + ", curdate())"); //rank is increased
            if(rank != POP_POSTS_RESULTS){ //comma at the end will break the query
                sqlInsertQuery.append(", ");
            }
        }
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlInsertQuery.toString());){
            int rowRecord = 1;
            while(rows.next()){
                int popPostId = rows.getInt(POST_ID);
                ps.setInt(rowRecord++, popPostId);
            }
            //System.out.println(sqlInsertQuery);
            ps.executeUpdate();
        }catch(SQLException e){
          log.error(e.getMessage());
        }
    }

    //метод който запазва списък от най-популярни хора
    @Override
    public void saveRecordOfMostPopularUsers(){
        String sqlQuery = "SELECT u.id AS " + USER_ID + ", u.username, u.first_name, u.last_name, u.age, COUNT(usu.subscriber_id) AS subscribers\n" +
                "FROM users AS u\n" +
                "INNER JOIN users_subscribe_users AS usu\n" +
                "ON (usu.subscribed_id = u.id)\n" +
                "GROUP BY usu.subscribed_id\n" +
                "ORDER BY subscribers DESC LIMIT " + POP_USERS_RESULTS;
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            ResultSet rows = ps.executeQuery();
            insertIntoMostPopularUsersTable(rows);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private void insertIntoMostPopularUsersTable(ResultSet rows){
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO most_popular_users (user_id, ranking, date_ranking)\n" +
                "VALUES ");
        for(int i = 1; i <= POP_USERS_RESULTS; i++){
            sqlInsertQuery.append("\n(?, " + i + ", curdate())");
            //comma at the end will break the query
            if(i != POP_POSTS_RESULTS){
                sqlInsertQuery.append(", ");
            }
        }
        try(Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlInsertQuery.toString());) {
            int rowCorrespondingToRank = 1;
            while(rows.next()){
                ps.setInt(rowCorrespondingToRank++, rows.getInt(USER_ID));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void saveRecordSignups() {
        String sqlQuerySelectNewUsersByAgeGroup =
                "SELECT COUNT(id) AS " + COUNT_NEW_USERS + ", "+ AGE +" \n" +
                "FROM users AS u \n" +
                "WHERE u.created_at BETWEEN date_sub(curdate(), "
                        + " INTERVAL " + RECORD_SIGNUPS_INTERVAL_DAYS + " DAY) AND curdate() \n" +
                "GROUP BY u.age;";
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlQuerySelectNewUsersByAgeGroup);
            ResultSet rows = ps.executeQuery();
            insertIntoTableSignups(rows);
        } catch (SQLException e) {
           log.error(e.getMessage());
        }
    }

    private void insertIntoTableSignups(ResultSet rows){
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO new_users_count_by_age \n" +
                "(record_created_at, count_new_users, age)\n" +
                "VALUES ");
        PreparedStatement ps = null;
        try(Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            if(rows.next()) { //ако е първото не се слага запетайка, за да не се счупи заявката
                sqlInsertQuery.append("(curdate(), " + rows.getInt(COUNT_NEW_USERS) + ", " + rows.getInt(AGE) + ")");
            } else{
                return; //no one has joined the website
            }
            while(rows.next()){
                sqlInsertQuery.append(",\n(curdate(), " + rows.getInt(1) + ", "+ rows.getInt(2) + ")");
            }
            ps = connection.prepareStatement(sqlInsertQuery.toString());
            ps.executeUpdate();
        }catch(SQLException e){
           log.error(e.getMessage());
        }finally{
            try {
                ps.close();
            } catch (SQLException e) {
               log.error(e.getMessage());
            }
        }
    }

    //метод който връща брой нови регистрации според възрастова група на последователите
    @Override
    public List<StatsProfile> getFavouriteProfilesByAgeGroup(int minRange, int maxRange) {
        String sqlSelectQuery = "SELECT influencers.id AS "+ USER_ID +", COUNT(subER.username) AS "+SUBSCRIBERS+"\n" +
                "FROM users AS subER\n" +
                "INNER JOIN users_subscribe_users AS usu\n" +
                "ON(subER.id = usu.subscriber_id)\n" +
                "INNER JOIN users AS influencers\n" +
                "ON(usu.subscribed_id = influencers.id)\n" +
                "WHERE subER.age BETWEEN "+ minRange +" AND " + maxRange + "\n" +
                "AND influencers.is_deleted = 0\n" +
                "GROUP BY "+ USER_ID +"\n" +
                "ORDER BY subscribers DESC\n" +
                "LIMIT " + POP_USERS_RESULTS;
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlSelectQuery);
            ResultSet rows = ps.executeQuery();
            List<StatsProfile> statsProfiles = new ArrayList<>();
            while(rows.next()) {
                long userId = rows.getInt(USER_ID);
                User user = userRepo.getById(userId);
                int subscribers = rows.getInt(SUBSCRIBERS);
                statsProfiles.add(new StatsProfile(user, subscribers)); //todo map
            }
            return statsProfiles;
        } catch(SQLException e){
            log.error(e.getMessage());
            throw new TechnicalIssuesException("Database on fire. Call the tech guy!");
        }
    }

    @Override
    public StatsSignups getSignupsCountByAgeRange(int minAge, int maxAge, int intervalDays) {
        String sqlQuery = "SELECT SUM(count_new_users) AS sum FROM new_users_count_by_age \n" +
                "WHERE age BETWEEN " + minAge + " AND " + maxAge + " \n" +
                "AND record_created_at BETWEEN date_sub(curdate(), INTERVAL " + intervalDays + " DAY) AND curdate()";
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rows = ps.executeQuery();
            rows.next();
            StatsSignups stats = new StatsSignups();
            stats.setMaxAge(maxAge);
            stats.setMinAge(minAge);
            stats.setPeriodDays(intervalDays);
            stats.setNewAccountsCreated(rows.getInt("sum"));
            return stats;
        } catch(SQLException e){
           log.error(e.getMessage());
            throw new TechnicalIssuesException("Database on fire. Call the tech guy!");
        }
    }
}
