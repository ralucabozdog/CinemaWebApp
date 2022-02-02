package database;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

//import org.springframework.jdbc.core.JdbcTemplate;

/*
@Component
public class DatabaseHandler {

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    public List<Movie> findAll() {
        return jdbcTemplate.query("SELECT * FROM cinema.movie", (rs, rowNum) -> new Movie(rs.getString("title"),
                rs.getString("poster"), rs.getFloat("rating"),
                rs.getString("descript"), rs.getString("cast"),
                rs.getString("director"), rs.getString("writer"),
                rs.getString("genre"), rs.getString("trailerURL"),
                rs.getString("duration"), rs.getString("classification")));
    }

    /*public void update(Customer customer) {
        jdbcTemplate.update(
                "UPDATE customers SET first_name=?, last_name=? WHERE id=?",
                customer.getFirstName(), customer.getLastName(), customer.getId());
    }*/

   /* public static void main(String[] args){
        DatabaseHandler databaseHandler = new DatabaseHandler();
        List<Movie> movieList = databaseHandler.findAll();
        for(Movie x : movieList)
            System.out.println(x);
    }*/


//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan("database")
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

    @Autowired
    Environment environment;

    private final String URL = "url";
    private final String USER = "dbuser";
    private final String DRIVER = "driver";
    private final String PASSWORD = "dbpassword";

    @Bean
    DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USER));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        return driverManagerDataSource;
    }
}

