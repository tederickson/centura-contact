package com.centura.contact.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.centura.contact.domain.ContactDigest;
import com.centura.contact.exception.ContactNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactServiceTest {

	@Autowired
	private ContactService service;

	private Long id = 2L;

	@Test
	public void testFindAll() {
		List<ContactDigest> contacts = service.findAll();

		assertNotNull(contacts);

		for (ContactDigest contact : contacts) {
			if (contact.getId() == 1L) {
				assertEquals("Bob Haskel", contact.getName());
				assertEquals("3035551234", contact.getPhone());
			}
		}
	}

	@Test
	public void testFindById() throws ContactNotFoundException {
		ContactDigest digest = service.findById(id);

		assertNotNull(digest);
		assertEquals(id, digest.getId());
		assertEquals("Boba Loo", digest.getName());
		assertEquals("8015556874", digest.getPhone());
	}

	@Test(expected = ContactNotFoundException.class)
	public void testFindByIdUsingInvalidId() throws ContactNotFoundException {
		service.findById(3414L);
	}

}
