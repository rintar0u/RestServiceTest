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

import rest.model.Customer;
import rest.service.customer.GetCustomerById;

public class GetCustomerByIdTest {
	MockMvc mockMvc;

	@InjectMocks
	CustomerServiceController controller;

	@Mock
	GetCustomerById getCustomerById;

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

		when(this.getCustomerById.execute("123")).thenReturn(model);

		// Then
		this.mockMvc.perform(get("/service/customer/123", 1, 10)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())

		// Given
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("田中健二")))
				.andExpect(jsonPath("$.email", is("tanaka@abc.com")));

		verify(this.getCustomerById).execute("123");
	}

	@Test
	public void failure_notFound() throws Exception {
		// When

		// Then
		this.mockMvc.perform(get("/service/customer/456")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())

		// Given
				.andExpect(status().isNotFound());
	}
}