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

import rest.exception.ModelAlreadyExistsException;
import rest.model.Customer;
import rest.service.customer.AddCustomer;
import rest.service.customer.GetCustomerByKey;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AddCustomerTest {
	MockMvc mockMvc;

	@InjectMocks
	CustomerServiceController controller;

	@Mock
	AddCustomer addCustomer;

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
		this.mockMvc.perform(post("/service/customer")
				.content(content)
                .contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())

		// Given
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("田中健二")))
				.andExpect(jsonPath("$.email", is("tanaka@abc.com")));

		verify(this.addCustomer).execute((Customer)any());
		verify(this.getCustomerByKey).execute("tanaka@abc.com");
	}

	@Test
	public void conflict() throws Exception {
		// When
		Customer model = new Customer("田中健二", "tanaka@abc.com");
		String content = mapper.writeValueAsString(model);

		doThrow(new ModelAlreadyExistsException()).when(this.addCustomer).execute((Customer)any());
		when(this.getCustomerByKey.execute("tanaka@abc.com")).thenReturn(model);

		// Then
		this.mockMvc.perform(post("/service/customer")
				.content(content)
                .contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())

		// Given
				.andExpect(status().isConflict());

		verify(this.addCustomer).execute((Customer)any());
	}


}