package database.customer;

import model.Customer;

import java.util.List;

public interface CustomerDAO {
    Customer getCustomerById(Integer id);

    List<Customer> getCustomerByUsername(String customerUsername);

    List<Customer> getAllCustomers();

    boolean deleteCustomer(Customer customer);

    boolean updateCustomer(Customer customer);

    boolean createCustomer(Customer customer);

    Integer getId(String username);
}
