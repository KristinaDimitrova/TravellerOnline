package traveller.model.dao.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import traveller.exception.TechnicalIssuesException;
import traveller.model.dao.AbstractDao;
import traveller.model.pojo.StatsProfile;
import traveller.model.pojo.User;
import traveller.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Component
public class StatsDBDao extends AbstractDao implements StatsDao {
    private static final int mostLikedPostsDay = 10; //extracted for flexibility
    private static final int mostPopularUsers = 10;
    private static final int recordSignUpsInterval = 7;

    @Autowired
    private UserRepository userRepo;

    //най-популярните постове за деня
    @Override
    public void saveRecordOfMostPopularPosts(){ //todo periodically
        String sqlQuerySelectTopPosts = "SELECT p.id, p.created_at, u.id AS post_owner_id, " +
                "COUNT(ulp.owner_id) AS likes FROM posts AS p\n" +
                "INNER JOIN location_types AS lt\n" +
                "ON(p.location_type_id = lt.id)\n" +
                "INNER JOIN users AS u\n" +
                "ON(u.id=p.owner_id)\n" +
                "INNER JOIN users_like_posts AS ulp\n" +
                "ON(p.id = ulp.post_id)\n" +
                "GROUP BY p.id\n" +
                "HAVING created_at BETWEEN DATE_SUB(NOW(), INTERVAL 1 DAY) AND NOW()\n" +
                "ORDER BY likes DESC LIMIT " + mostLikedPostsDay;
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlQuerySelectTopPosts);
            ResultSet rows = ps.executeQuery();
            insertIntoTopPostsTable(rows);
        } catch (SQLException e) {
            System.out.println("Deeba");
            //todo запиши в log
        }
    }

    private void insertIntoTopPostsTable(ResultSet rows){
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO top_ten_posts (post_id, ranking, date)\n" +
                "VALUES \n");
        for(int k = 1; k <= mostLikedPostsDay; k++){
            sqlInsertQuery.append("(?, " + k + ", curdate())");
            //comma at the end will break the query
            if(k != mostLikedPostsDay){
                sqlInsertQuery.append(", ");
            }
        }
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlInsertQuery.toString());){
            int rank = 1;
            while(rows.next()){
                ps.setInt(rank++, rows.getInt(1));
            }
        }catch(SQLException e){
            //todo log method
        }
    }

    //метод който запазва списък от най-популярни хора
    @Override
    public void saveRecordMostPopularUsers(){ //todo periodically
        String sqlQuery = "SELECT u.id, u.username, u.first_name, u.last_name, u.age, COUNT(usu.subscriber_id) AS subscribers\n" +
                "FROM users AS u\n" +
                "INNER JOIN users_subscribe_users AS usu\n" +
                "ON (usu.subscribed_id = u.id)\n" +
                "GROUP BY usu.subscribed_id\n" +
                "ORDER BY subscribers DESC LIMIT " + mostPopularUsers;
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlQuery)) {
            ResultSet rows = ps.executeQuery();
            insertIntoMostPopularUsersTable(rows);
        } catch (SQLException e) {
            //todo запиши в log
        }
    }

    private void insertIntoMostPopularUsersTable(ResultSet rows){
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO most_popular_users (user_id, ranking, date_ranking)\n" +
                "VALUES \n");
        for(int i = 1; i <= mostPopularUsers; i++){
            sqlInsertQuery.append("(?, " + i + ", curdate()) \n");
            //comma at the end will break the query
            if(i != mostLikedPostsDay){
                sqlInsertQuery.append(", ");
            }
        }
        try(Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = connection.prepareStatement(sqlInsertQuery.toString());) {
            int rank = 1;
            while(rows.next()){
                ps.setInt(rank++, rows.getInt(1));
            }
        } catch (SQLException e) {
            //todo log
            //try with resources is needed here, because connection has to be closed
        }
    }

    @Override
    public void saveRecordOfNewUsersByAgeGroup() { //todo periodically
        String sqlQuerySelectNewUsersByAgeGroup =
                "SELECT COUNT(id) AS new_users_count, age \n" +
                "FROM users AS u \n" +
                "WHERE u.created_at BETWEEN date_sub(curdate(), "
                        + " INTERVAL " + recordSignUpsInterval + " DAY) AND curdate() \n" +
                "GROUP BY u.age;";
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlQuerySelectNewUsersByAgeGroup);
            ResultSet rows = ps.executeQuery();
            insertIntoTableSignups(rows);
        } catch (SQLException e) {
            System.out.println(" NOTT GGOOD");
            //todo запиши в log
        }
    }

    private void insertIntoTableSignups(ResultSet rows){
        // new_users_count , age ,
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO new_users_count_by_age \n" +
                "(record_created_at, count_new_users, age)\n" +
                "VALUES \n");
        PreparedStatement ps = null;
        try(Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            //ако е първото не се слага запетайка, за да не се счупи заявката
            if(rows.next()) {
                sqlInsertQuery.append(",\n (curdate(), " + rows.getInt(1) + ", " + rows.getInt(2) + ")");
            }
            while(rows.next()){
                sqlInsertQuery.append(",(curdate(), " + rows.getInt(1) + ", "+ rows.getInt(2) + ")");
            }
            ps = connection.prepareStatement(sqlInsertQuery.toString());
            ps.executeUpdate();
        }catch(SQLException e){
            //todo log
        }finally{
            try {
                ps.close();
            } catch (SQLException e) {
                //todo log
            }
        }
    }

    //метод който връща брой нови регистрации според възрастова група
    @Override
    public List<StatsProfile> getFavouriteProfilesByAgeGroup(int minRange, int maxRange) { //todo use in service
        String sqlSelectQuery = "SELECT influencers.id AS influencer_id, COUNT(subER.username) AS subscribers\n" +
                "FROM users AS subER\n" +
                "INNER JOIN users_subscribe_users AS usu\n" +
                "ON(subER.id = usu.subscriber_id)\n" +
                "INNER JOIN users AS influencers\n" +
                "ON(usu.subscribed_id = influencers.id)\n" +
                "WHERE subER.age BETWEEN "+ minRange +" AND " + maxRange + "\n" +
                "AND influencers.is_deleted = 0\n" +
                "GROUP BY influencers.id\n" +
                "ORDER BY subscribers DESC\n" +
                "LIMIT " + mostPopularUsers;
        System.out.println("OKAY. FUCK IT IS THE QUERY");
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            System.out.println("I established connection.");
            PreparedStatement ps = conn.prepareStatement(sqlSelectQuery);
            System.out.println("I prepared a statement.");
            ResultSet rows = ps.executeQuery(); //връща се ResultSet, за да не се конструират листове от DTO-та в DAO-то
            List<StatsProfile> statsProfiles = new ArrayList<>();
            System.out.println("Okay here 178");
            while(rows.next()) {
                long userId = rows.getInt("influencer_id");
                System.out.println("user id -> " + userId);
                System.out.println("User repository has to get the user now.");
                User user = userRepo.getById(userId);
                System.out.println("Okay here 181");
                int subscribers = rows.getInt("subscribers");
                System.out.println("Okay here 183");
                statsProfiles.add(new StatsProfile(user, subscribers));
                System.out.println("Okay here 185"); //todo delete souts after final testing
            }
            System.out.println("Okay here 187");
            return statsProfiles;
        } catch(SQLException e){
            //todo log
            throw new TechnicalIssuesException("Database on fire. Call the tech guy!");
        }
    }
}
