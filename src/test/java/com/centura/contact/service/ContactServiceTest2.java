package com.centura.contact.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.exception.ContactNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactServiceTest2 {

	@Autowired
	private ContactService2 service;

	private Long id = 2L;

	@Test
	public void testFindAll() {
		List<ContactDigest2> contacts = service.findAll(0, 100);

		assertNotNull(contacts);

		for (ContactDigest2 contact : contacts) {
			if (contact.getId() == 1L) {
				assertEquals("Bob", contact.getFirstName());
				assertEquals("Haskel", contact.getLastName());
				assertEquals("3035551234", contact.getPhone());
			}
		}
	}

	@Test
	public void testFindById() throws ContactNotFoundException {
		ContactDigest2 contact = service.findById(id);

		assertNotNull(contact);
		assertEquals(id, contact.getId());
		assertEquals("Boba", contact.getFirstName());
		assertEquals("Loo", contact.getLastName());
		assertEquals("8015556874", contact.getPhone());
	}

	@Test(expected = ContactNotFoundException.class)
	public void testFindByIdUsingInvalidId() throws ContactNotFoundException {
		service.findById(3414L);
	}

}
