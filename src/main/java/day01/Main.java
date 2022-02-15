// tábla létrehozása HeidiSQL-ben először:
// CREATE TABLE actors(id BIGINT AUTO_INCREMENT, actor_name VARCHAR(255), CONSTRAINT pk_actors PRIMARY KEY (id));


package day01;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/movies-actors?useUnicode=true");
            dataSource.setUser("root");
            dataSource.setPassword("root");
        }
        catch (SQLException sqle) {
            throw new IllegalStateException("Cannot reach database!", sqle);
        }

        try (Connection connection = dataSource.getConnection()){
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("insert into actors(actor_name) values('John Doe')");
        }
        catch (SQLException sqle){
            throw new IllegalStateException("Cannot connect!", sqle);
        }

    }

}
