package rest.service.customer;

import rest.model.Customer;

public interface GetCustomerByKey {
	Customer execute(String email);
}