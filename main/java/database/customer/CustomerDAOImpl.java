package database.customer;

import database.movie.MovieDAO;
import database.movie.MovieMapper;
import model.Customer;
import model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class CustomerDAOImpl implements CustomerDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_CUSTOMER = "select * from customer where idcustomer = ?";
    private final String SQL_FIND_CUSTOMER_BY_USERNAME = "select * from customer where username = ?";
    private final String SQL_DELETE_CUSTOMER = "delete from customer where username = ?";
    private final String SQL_UPDATE_CUSTOMER = "update customer set firstname = ?, lastname = ?, password = ? where username = ?";
    private final String SQL_GET_ALL = "select * from customer";
    private final String SQL_INSERT_CUSTOMER = "insert into customer(username, password, firstname, lastname, usertype) values(?,?,?,?,?)";
    private final String SQL_GET_ID = "select idcustomer from customer where username = ?";

    @Autowired
    public CustomerDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Customer getCustomerById(Integer id) {
        return jdbcTemplate.queryForObject(SQL_FIND_CUSTOMER, new Object[] { id }, new CustomerMapper());
    }

    public List<Customer> getCustomerByUsername(String customerUsername) {
        return jdbcTemplate.query(SQL_FIND_CUSTOMER_BY_USERNAME, new Object[] { customerUsername }, new CustomerMapper());
    }

    public List<Customer> getAllCustomers() {
        return jdbcTemplate.query(SQL_GET_ALL, new CustomerMapper());
    }

    public boolean deleteCustomer(Customer customer) {
        return jdbcTemplate.update(SQL_DELETE_CUSTOMER, customer.getUsername()) > 0;
    }

    public boolean updateCustomer(Customer customer) {
        return jdbcTemplate.update(SQL_UPDATE_CUSTOMER, customer.getFirstName(), customer.getLastName(), customer.getPassword(),
                customer.getUsername()) > 0;
    }

    public boolean createCustomer(Customer customer) {
        return jdbcTemplate.update(SQL_INSERT_CUSTOMER, customer.getUsername(), customer.getPassword(), customer.getFirstName(), customer.getLastName(), customer.getUserType()) > 0;
    }

    public Integer getId(String customerUsername) {
        return jdbcTemplate.queryForObject(SQL_GET_ID, new Object[] { customerUsername }, Integer.class);
    }
}
