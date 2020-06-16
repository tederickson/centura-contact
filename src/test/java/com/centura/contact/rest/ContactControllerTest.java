package com.centura.contact.rest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.centura.contact.ContactsApplication;
import com.centura.contact.domain.ContactDigest;
import com.centura.contact.service.ContactService;
import com.centura.contact.service.ContactService2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@RunWith(SpringRunner.class)
@WebMvcTest(ContactController.class)
@ComponentScan(basePackageClasses = ContactsApplication.class)
public class ContactControllerTest {
	private final static String VERSION = "/v1/";
	private final static Long DEFAULT_ID = 123L;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ContactService contactService;

	@MockBean
	private ContactService2 notUsedService;

	@Test
	public void testGetContactByIdInvalidId() throws Exception {
		MockHttpServletRequestBuilder builder = get(VERSION + "contact/-123456").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().is4xxClientError());
	}

	@Test
	public void testGetContactByIdMissingId() throws Exception {
		MockHttpServletRequestBuilder builder = get(VERSION + "contact").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().is4xxClientError());
	}

	@Test
	public void testGetContactById() throws Exception {
		ContactDigest expected = buildContactDigest();

		when(contactService.findById(DEFAULT_ID)).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get(VERSION + "contact/" + DEFAULT_ID).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();

		String json = result.getResponse().getContentAsString();
		ContactDigest actual = new ObjectMapper().readValue(json, ContactDigest.class);

		assertEquals(expected, actual);
	}

	@Test
	public void testGetContactsNoResults() throws Exception {
		MockHttpServletRequestBuilder builder = get(VERSION + "contacts").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();

		assertEquals("[]", json);
	}

	@Test
	public void testGetContacts() throws Exception {
		List<ContactDigest> expected = new ArrayList<>();
		expected.add(buildContactDigest());
		expected.add(buildContactDigest(15L, "Al Jackson", "8015551234"));

		when(contactService.findAll()).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get(VERSION + "contacts").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, ContactDigest.class);
		List<ContactDigest> actual = new ObjectMapper().readValue(json, javaType);

		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
	}

	private ContactDigest buildContactDigest() {
		return buildContactDigest(DEFAULT_ID, "Fred Alphonse", "3035551234");
	}

	private ContactDigest buildContactDigest(Long id, String name, String phone) {
		ContactDigest digest = new ContactDigest();

		digest.setId(id);
		digest.setName(name);
		digest.setPhone(phone);

		return digest;
	}
}
