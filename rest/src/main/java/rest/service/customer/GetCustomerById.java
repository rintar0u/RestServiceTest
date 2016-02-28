package rest.service.customer;

import rest.model.Customer;

public interface GetCustomerById {
	Customer execute(String id);
}