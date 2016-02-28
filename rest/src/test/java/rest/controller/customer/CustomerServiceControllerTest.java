package rest.controller.customer;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import rest.model.Customer;
import rest.service.customer.GetCustomerList;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerServiceControllerTest {
  MockMvc mockMvc;

  @InjectMocks
  CustomerServiceController controller;

  @Mock
  GetCustomerList getCustomerList;

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
    List<Customer> list = Arrays.asList(new Customer[] {
        new Customer("田中健二", "tanaka@abc.com"),
        new Customer("斎藤裕太", "saitou@abc.com"),
        new Customer("星野真司", "hoshino@abc.com"),
    });

    when(this.getCustomerList.execute(any(Pageable.class))).thenReturn(list);

    // Then
    this.mockMvc.perform(get("/service/customer/page/{page}/size/{size}", 1, 10)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(3)))
        .andExpect(jsonPath("$[0].name", is("田中健二")))
        .andExpect(jsonPath("$[0].email", is("tanaka@abc.com")))
        .andExpect(jsonPath("$[1].name", is("斎藤裕太")))
        .andExpect(jsonPath("$[1].email", is("saitou@abc.com")))
        .andExpect(jsonPath("$[2].name", is("星野真司")))
        .andExpect(jsonPath("$[2].email", is("hoshino@abc.com")));

    verify(this.getCustomerList).execute(new PageRequest(0, 10));
  }

  @Test
  public void invalidPage() throws Exception {
    // When

    // Then
    this.mockMvc.perform(get("/service/customer/page/{page}/size/{size}", "a", 10)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(status().isBadRequest());
  }

  @Test
  public void invalidSize() throws Exception {
    // When

    // Then
    this.mockMvc.perform(get("/service/customer/page/{page}/size/{size}", 1, "a")
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())

    // Given
        .andExpect(status().isBadRequest());
  }
}