package database.price;

import model.Price;
import model.Theatre;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceMapper implements RowMapper<Price> {

    public Price mapRow(ResultSet rs, int i) throws SQLException {

        Price price = new Price(rs.getFloat("amount"), rs.getString("typeofplay"));
        return price;
    }
}
