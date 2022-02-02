package database.price;

import database.movie.MovieMapper;
import database.theatre.TheatreDAO;
import database.theatre.TheatreMapper;
import model.Price;
import model.Theatre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class PriceDAOImpl implements PriceDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_UPDATE_PRICE = "update price set amount = ? where typeofplay = ?";
    private final String SQL_GET_ALL = "select * from price";
    private final String SQL_GET_PRICE_OF_REPRESENTATION = "select amount from price where typeofplay = ?";

    @Autowired
    public PriceDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean updatePrice(float amount, String typeOfPlay){
        return jdbcTemplate.update(SQL_UPDATE_PRICE, amount, typeOfPlay) > 0;
    }

    public List<Price> getAllPrices(){
        return jdbcTemplate.query(SQL_GET_ALL, new PriceMapper());
    }

    public float getPriceForTypeOfPlay(String typeOfPlay){
        return jdbcTemplate.queryForObject(SQL_GET_PRICE_OF_REPRESENTATION, new Object[]{typeOfPlay}, Float.class);
    }
}
