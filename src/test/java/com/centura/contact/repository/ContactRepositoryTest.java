package com.centura.contact.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.centura.contact.model.Contact;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ContactRepositoryTest {

	@Autowired
	private ContactRepository repository;

	private Long id = 2L;

	@Test
	public void testFindAll() {
		List<Contact> contacts = repository.findAll();

		assertNotNull(contacts);

		for (Contact contact : contacts) {
			if (contact.getId() == 1L) {
				assertEquals("Bob Haskel", contact.getName());
				assertEquals("Bob", contact.getFirstName());
				assertEquals("Haskel", contact.getLastName());
				assertEquals("3035551234", contact.getPhone());
			}
		}
	}

	@Test
	public void testFindById() {
		Optional<Contact> entity = repository.findById(id);

		assertTrue(entity.isPresent());

		Contact contact = entity.get();

		assertEquals(id, contact.getId());
		assertEquals("Boba Loo", contact.getName());
		assertEquals("Boba", contact.getFirstName());
		assertEquals("Loo", contact.getLastName());
		assertEquals("8015556874", contact.getPhone());
	}

	@Test
	public void testExistsById() {
		assertTrue(repository.existsById(id));

		assertFalse(repository.existsById(1234L));
	}

	@Test
	public void testCount() {
		assertEquals(3, repository.count());
	}

}
