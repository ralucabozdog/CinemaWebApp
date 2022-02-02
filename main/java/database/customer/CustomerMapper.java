package database.customer;

import model.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {

    public Customer mapRow(ResultSet rs, int i) throws SQLException {

        Customer customer = new Customer(rs.getString("username"),
                rs.getString("password"), rs.getString("firstName"),
                rs.getString("lastName"), rs.getInt("usertype"));
        return customer;
    }
}
