package com.centura.contact.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.centura.contact.domain.ContactDigest;
import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.exception.InvalidContactIdParameterException;
import com.centura.contact.service.ContactService;
import com.centura.contact.service.ContactService2;

import lombok.extern.slf4j.Slf4j;

/**
 * Exceptions are handled by the ContactControllerAdvice. The exception is
 * logged and converted to the appropriate HTTP Status code.
 */

@RestController
@Slf4j
public class ContactController {
	@Autowired
	private ContactService contactService;

	@Autowired
	private ContactService2 contactService2;

	@GetMapping("/v1/contact/{id}")
	public ContactDigest getContactByIdVersion1(@PathVariable("id") Long id)
			throws ContactNotFoundException, InvalidContactIdParameterException {
		log.info("/v1/contact/" + id);

		validateId(id);

		return contactService.findById(id);
	}

	@GetMapping("/v1/contacts")
	public List<ContactDigest> getContactsVersion1() {
		log.info("/v1/contacts/");

		return contactService.findAll();
	}

	@GetMapping("/v2/contact/{id}")
	public ContactDigest2 getContactById(@PathVariable("id") Long id) throws ContactNotFoundException, InvalidContactIdParameterException {
		log.info("/v2/contact/" + id);

		validateId(id);

		return contactService2.findById(id);
	}

	@GetMapping("/v2/contacts")
	public List<ContactDigest2> getContacts() {
		log.info("/v2/contacts/");

		return contactService2.findAll();
	}

	private void validateId(Long id) throws InvalidContactIdParameterException {
		if (id < 0) {
			throw new InvalidContactIdParameterException("id must be positive");
		}
	}
}
