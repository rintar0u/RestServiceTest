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

import rest.service.customer.GetCustomerCount;

public class GetCustomerCountTest {
	MockMvc mockMvc;

	@InjectMocks
	CustomerServiceController controller;

	@Mock
	GetCustomerCount getCustomerCount;

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
		when(this.getCustomerCount.execute()).thenReturn(3L);

		// Then
		this.mockMvc.perform(get("/service/customer/count")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())

		// Given
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.count", is(3)));

		verify(this.getCustomerCount).execute();
	}
}