package rest.service.customer;

import java.util.List;

import org.springframework.data.domain.Pageable;

import rest.model.Customer;

public interface GetCustomerList {
	List<Customer> execute(Pageable page);
}