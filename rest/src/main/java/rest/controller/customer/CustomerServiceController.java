package rest.controller.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import rest.exception.ModelAlreadyExistsException;
import rest.exception.ModelNotFoundException;
import rest.model.Customer;
import rest.service.customer.AddCustomer;
import rest.service.customer.GetCustomerById;
import rest.service.customer.GetCustomerByKey;
import rest.service.customer.GetCustomerCount;
import rest.service.customer.GetCustomerList;
import rest.service.customer.RemoveCustomer;
import rest.service.customer.UpdateCustomer;

@Controller
@RequestMapping(value = "/service/customer")
public class CustomerServiceController {

  private static Logger logger = LoggerFactory.getLogger("Rest");

  @Autowired
  private GetCustomerList getCustomerList;

  @Autowired
  private GetCustomerCount getCustomerCount;

  @Autowired
  private GetCustomerById getCustomerById;

  @Autowired
  private GetCustomerByKey getCustomerByKey;

  @Autowired
  private AddCustomer addCustomer;

  @Autowired
  private RemoveCustomer removeCustomer;

  @Autowired
  private UpdateCustomer updateCustomer;

  @ResponseBody
  @RequestMapping(value = "page/{page}/size/{size}", method = RequestMethod.GET)
  public List<Customer> getList(
      @PathVariable("page") int page,
      @PathVariable("size") int size) {
    logger.info("list");

    return this.getCustomerList.execute(new PageRequest(page - 1, size));
  }

  @ResponseBody
  @RequestMapping(value = "count", method = RequestMethod.GET)
  public Map<String, Object> getCount() {
    logger.info("count");

    long count = this.getCustomerCount.execute();

    Map<String, Object> result = new HashMap<>();
    result.put("count", count);
    return result;
  }

  @ResponseBody
  @RequestMapping(value = "{id}", method = RequestMethod.GET)
  public ResponseEntity<Customer> getById(
      @PathVariable("id") String id) {
    logger.info("getById");

    Customer model = this.getCustomerById.execute(id);
    if (model == null) {
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<Customer>(model, HttpStatus.OK);
  }

  @ResponseBody
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Customer> add(
      @RequestBody Customer model) {
    logger.info("add");

    try {
      this.addCustomer.execute(model);
    }
    catch (ModelAlreadyExistsException ex) {
      return new ResponseEntity<Customer>(HttpStatus.CONFLICT);
    }

    String key = model.getEmail();
    Customer added = this.getCustomerByKey.execute(key);
    if (added == null) {
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<Customer>(added, HttpStatus.CREATED);
  }

  @ResponseBody
  @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Map<String, Object>> remove(
      @PathVariable("id") String id) {
    logger.info("remove");

    try {
      this.removeCustomer.execute(id);
    }
    catch (ModelNotFoundException ex) {
      return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
    }

    Map<String, Object> result = new HashMap<>();
    result.put("result", "success");

    return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
  }

  @ResponseBody
  @RequestMapping(method = RequestMethod.PUT)
  public ResponseEntity<Customer> update(
      @RequestBody Customer model) {
    logger.info("update");

    try {
      this.updateCustomer.execute(model);
    }
    catch (ModelNotFoundException ex) {
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }

    String key = model.getEmail();
    Customer added = this.getCustomerByKey.execute(key);
    if (added == null) {
      return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<Customer>(added, HttpStatus.OK);
  }
}