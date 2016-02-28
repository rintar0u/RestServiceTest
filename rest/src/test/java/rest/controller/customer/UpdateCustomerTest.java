package rest.controller.customer;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import rest.exception.ModelNotFoundException;
import rest.model.Customer;
import rest.service.customer.GetCustomerByKey;
import rest.service.customer.UpdateCustomer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UpdateCustomerTest {
  MockMvc mockMvc;

  @InjectMocks
  CustomerServiceController controller;

  @Mock
  UpdateCustomer updateCustomer;

  @Mock
  GetCustomerByKey getCustomerByKey;

  ObjectMapper mapper = new ObjectMapper();

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller)
        .setMessageConverters(new MappingJackson2HttpMessageConverter())
        .build();
  }

  @Test
  public void success() throws Exception {
    // When
    Customer model = new Customer("田中健二", "tanaka@abc.com");
    String content = mapper.writeValueAsString(model);

    when(this.getCustomerByKey.execute("tanaka@abc.com")).thenReturn(model);

    // Then
    this.mockMvc.perform(put("/service/customer")
        .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("田中健二")))
        .andExpect(jsonPath("$.email", is("tanaka@abc.com")));

    verify(this.updateCustomer).execute((Customer)any());
    verify(this.getCustomerByKey).execute("tanaka@abc.com");
  }

  @Test
  public void notFound() throws Exception {
    // When
    Customer model = new Customer("田中健二", "tanaka@abc.com");
    String content = mapper.writeValueAsString(model);

    doThrow(new ModelNotFoundException()).when(this.updateCustomer).execute((Customer)any());
    when(this.getCustomerByKey.execute("tanaka@abc.com")).thenReturn(model);

    // Then
    this.mockMvc.perform(put("/service/customer")
        .content(content)
                .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(status().isNotFound());

    verify(this.updateCustomer).execute((Customer)any());
  }


}