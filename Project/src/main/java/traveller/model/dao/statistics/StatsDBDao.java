package traveller.model.dao.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import traveller.model.dao.AbstractDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
@Component
public class StatsDBDao extends AbstractDao {
    private static final int mostLikedPostsDay = 10; //extracted for flexibility
    private static final int mostPopularUsers = 10;

    //най-популярните постове за деня
    public void saveRecordOfMostPopularPosts(){
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
            //todo трябва ли да се хвърля user-friendly exceptions
        }
    }

    private void insertIntoTopPostsTable(ResultSet rows) throws SQLException {
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO top_ten_posts (post_id, ranking, date)\n" +
                "VALUES \n");
        for(int k = 1; k <= mostLikedPostsDay; k++){
            sqlInsertQuery.append("(?, " + k + ", curdate())");
            //comma at the end will break the query
            if(k != mostLikedPostsDay){
                sqlInsertQuery.append(", ");
            }
        }
        Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        PreparedStatement ps = conn.prepareStatement(sqlInsertQuery.toString());
        int rank = 1;
        while(rows.next()){
            ps.setInt(rank++, rows.getInt(1));
        }
        //todo test
    }

    //метод който запазва списък от най-популярни хора
    public void saveRecordMostPopularUsers(){
        String sqlQuery = "SELECT u.id, u.username, u.first_name, u.last_name, u.age, COUNT(usu.subscriber_id) AS subscribers\n" +
                "FROM users AS u\n" +
                "INNER JOIN users_subscribe_users AS usu\n" +
                "ON (usu.subscribed_id = u.id)\n" +
                "GROUP BY usu.subscribed_id\n" +
                "ORDER BY subscribers DESC LIMIT " + mostPopularUsers;
        try(Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet rows = ps.executeQuery();
            insertIntoMostPopularUsersTable(rows);
        } catch (SQLException e) {
            //todo запиши в log
            //todo трябва ли да се хвърля user-friendly exceptions
        }
    }

    private void insertIntoMostPopularUsersTable(ResultSet rows) throws SQLException{
        StringBuilder sqlInsertQuery = new StringBuilder("INSERT INTO most_popular_users (user_id, ranking, date_ranking)\n" +
                "VALUES \n");
        for(int i = 1; i <= mostPopularUsers; i++){
            sqlInsertQuery.append("(?, " + i + ", curdate()),\n");
            //comma at the end will break the query
            if(i != mostLikedPostsDay){
                sqlInsertQuery.append(", ");
            }
        }
        Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        PreparedStatement ps = connection.prepareStatement(sqlInsertQuery.toString());
        int rank = 1;
        while(rows.next()){
            ps.setInt(rank++, rows.getInt(1));
        }
        //todo test
    }

    //любими профили според възрастова група

    //метод който връща брой нови регистрации според възрастова група





}
