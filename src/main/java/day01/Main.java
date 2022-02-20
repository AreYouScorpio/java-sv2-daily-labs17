// tábla létrehozása HeidiSQL-ben először:
// CREATE TABLE actors(id BIGINT AUTO_INCREMENT, actor_name VARCHAR(255), CONSTRAINT pk_actors PRIMARY KEY (id));


package day01;

import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach database!", sqle);
        }

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        // tesztelés előtt
        // flyway.clean();
        flyway.migrate();

        ActorsRepository actorsRepository = new ActorsRepository(dataSource);

        MoviesRepository moviesRepository = new MoviesRepository(dataSource);

        // //4.nap off
        // moviesRepository.saveMovie("Titanic", LocalDate.of(1997, 12, 11));
        // moviesRepository.saveMovie("LOR", LocalDate.of(2000, 12, 23));

        //4.nap:

        ActorsMoviesRepository actorsMoviesRepository = new ActorsMoviesRepository(dataSource);

        ActorsMoviesService service = new ActorsMoviesService(actorsRepository, moviesRepository, actorsMoviesRepository);

        service.insertMovieWithActors("Titanic", LocalDate.of(1997, 11, 13), List.of("Leonardo DiCaprio", "Kate Winslet"));
        service.insertMovieWithActors("Great Gatsby", LocalDate.of(2012, 12, 11), List.of("Leonardo DiCaprio", "Toby"));

        // 4.nap off
        // System.out.println(moviesRepository.findAllMovies());

        /* // teszthez:
        actorsRepository.saveActor("John Doe");

        System.out.println(actorsRepository.findActorsWithPrefix("j"));

         */

/*
        try (Connection connection = dataSource.getConnection()){
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("insert into actors(actor_name) values('John Doe')");
        }
        catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect!", sqle);
        }

 */

    }

}
