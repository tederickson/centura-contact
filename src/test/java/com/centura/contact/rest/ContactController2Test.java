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
import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.service.ContactService;
import com.centura.contact.service.ContactService2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@RunWith(SpringRunner.class)
@WebMvcTest(ContactController2.class)
@ComponentScan(basePackageClasses = ContactsApplication.class)
public class ContactController2Test {
	private final static Long DEFAULT_ID = 123L;

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ContactService notUsedService;

	@MockBean
	private ContactService2 contactService;

	@Test
	public void testGetContactByIdVersion1InvalidId() throws Exception {
		MockHttpServletRequestBuilder builder = get("/v2/contact/-123456").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().is4xxClientError());
	}

	@Test
	public void testGetContactByIdVersion1MissingId() throws Exception {
		MockHttpServletRequestBuilder builder = get("/v2/contact").contentType(MediaType.APPLICATION_JSON);
		mvc.perform(builder).andExpect(status().is4xxClientError());
	}

	@Test
	public void testGetContactByIdVersion1() throws Exception {
		ContactDigest2 expected = buildContactDigest();

		when(contactService.findById(DEFAULT_ID)).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get("/v2/contact/" + DEFAULT_ID).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();

		String json = result.getResponse().getContentAsString();
		ContactDigest2 actual = new ObjectMapper().readValue(json, ContactDigest2.class);

		assertEquals(expected, actual);
	}

	@Test
	public void testGetContactsVersion1NoResults() throws Exception {
		MockHttpServletRequestBuilder builder = get("/v2/contacts").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();

		assertEquals("[]", json);
	}

	@Test
	public void testGetContactsVersion1() throws Exception {
		List<ContactDigest2> expected = new ArrayList<>();
		expected.add(buildContactDigest());
		expected.add(buildContactDigest(15L, "Al", "Jackson", "8015551234"));

		when(contactService.findAll(0, 40)).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get("/v2/contacts").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, ContactDigest2.class);
		List<ContactDigest2> actual = new ObjectMapper().readValue(json, javaType);

		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0), actual.get(0));
		assertEquals(expected.get(1), actual.get(1));
	}

	@Test
	public void testGetContactsVersion1SetStartPage() throws Exception {
		List<ContactDigest2> expected = new ArrayList<>();
		expected.add(buildContactDigest());

		when(contactService.findAll(3, 40)).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get("/v2/contacts?start=3").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, ContactDigest2.class);
		List<ContactDigest2> actual = new ObjectMapper().readValue(json, javaType);

		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0), actual.get(0));
	}

	@Test
	public void testGetContactsVersion1SetStartPageAndPageSize() throws Exception {
		List<ContactDigest2> expected = new ArrayList<>();
		expected.add(buildContactDigest());

		when(contactService.findAll(3, 6)).thenReturn(expected);

		MockHttpServletRequestBuilder builder = get("/v2/contacts?start=3&size=6").contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mvc.perform(builder).andExpect(status().isOk()).andReturn();
		String json = result.getResponse().getContentAsString();

		ObjectMapper mapper = new ObjectMapper();
		CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, ContactDigest2.class);
		List<ContactDigest2> actual = new ObjectMapper().readValue(json, javaType);

		assertEquals(expected.size(), actual.size());
		assertEquals(expected.get(0), actual.get(0));
	}

	private ContactDigest2 buildContactDigest() {
		return buildContactDigest(DEFAULT_ID, "Fred", "Alphonse", "3035551234");
	}

	private ContactDigest2 buildContactDigest(Long id, String firstName, String lastName, String phone) {
		ContactDigest2 digest = new ContactDigest2();

		digest.setId(id);
		digest.setFirstName(firstName);
		digest.setLastName(lastName);
		digest.setPhone(phone);

		return digest;
	}
}
