package day01;

import org.mariadb.jdbc.client.result.Result;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoviesRepository {

    private DataSource dataSource;

    public MoviesRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long saveMovie(String title, LocalDate releaseDate) {
        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement statement =
                     conn.prepareStatement
                             ("insert into movies(title, release_date) values(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, title);
            statement.setDate(2, Date.valueOf(releaseDate));
            statement.executeUpdate();

            try (
                    ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                throw new IllegalStateException("Insert failed to movies!");
            }

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect!", sqle);
        }
    }

    public List<Movie> findAllMovies() {
        // List<Movie> movies = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             //language=sql
             PreparedStatement statement = conn.prepareStatement("select * from movies");
             ResultSet rs = statement.executeQuery()) {

            /*
            while (rs.next()){
                long id = rs.getLong("id");
                String title = rs.getString("title");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                movies.add(new Movie(id, title, releaseDate));
            }
            // kiemeljük ezt a process-be

             */
            return processResultSet(rs);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot query!", sqle);
        }
        // return movies;
    }

    private List<Movie> processResultSet(ResultSet rs) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            String title = rs.getString("title");
            LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
            movies.add(new Movie(id, title, releaseDate));
        }
        return movies;
    }

    public Optional<Movie> findMovieByTitle(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmp = conn.prepareStatement("select * from movies where title=?")) {
            stmp.setString(1, title);
            try (ResultSet rs = stmp.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(rs.getLong("id"), rs.getString("title"), rs.getDate("release_date").toLocalDate()));
                } else return Optional.empty();
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot connect to movies", sqle);
        }

    }

    public double getMovieAvgRating(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "select avg(rating) as calculated_avg from ratings join movies on movies.id=ratings.movie_id where movies.title=?")) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("calculated_avg");
                }
                throw new IllegalArgumentException("Cannot find movie!");
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot quiery", sqle);
        }
        //return 0;
    }

    public void updateMovieAvgRating(String title, double avg) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE movies SET avg_rating=? where title=?")) {

            stmt.setDouble(1,avg);
            stmt.setString(2,title);
            stmt.executeUpdate();

        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot update!", sqle);
        }
    }

}
