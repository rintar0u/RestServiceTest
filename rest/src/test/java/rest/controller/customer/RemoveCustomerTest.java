package rest.controller.customer;

import static org.hamcrest.CoreMatchers.*;
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
import rest.service.customer.RemoveCustomer;

public class RemoveCustomerTest {
  MockMvc mockMvc;

  @InjectMocks
  CustomerServiceController controller;

  @Mock
  RemoveCustomer removeCustomer;

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

    // Then
    this.mockMvc.perform(delete("/service/customer/123")
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("success")));

    verify(this.removeCustomer).execute("123");
  }

  @Test
  public void notFound() throws Exception {
    // When
    doThrow(new ModelNotFoundException()).when(this.removeCustomer).execute("123");

    // Then
    this.mockMvc.perform(delete("/service/customer/123")
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(status().isNotFound());

    verify(this.removeCustomer).execute("123");
  }


}