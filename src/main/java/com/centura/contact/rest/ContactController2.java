package com.centura.contact.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.exception.InvalidContactIdParameterException;
import com.centura.contact.service.ContactService2;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for version 2 of the API.
 */

@RestController
@Slf4j
public class ContactController2 {
	@Autowired
	private ContactService2 contactService;

	@GetMapping("/v2/contact/{id}")
	public ContactDigest2 getContactById(@PathVariable("id") Long id) throws ContactNotFoundException, InvalidContactIdParameterException {
		log.info("/v2/contact/" + id);

		validateId(id);

		return contactService.findById(id);
	}

	@GetMapping("/v2/contacts")
	public List<ContactDigest2> getContacts() {
		log.info("/v2/contacts/");

		return contactService.findAll();
	}

	private void validateId(Long id) throws InvalidContactIdParameterException {
		if (id < 0) {
			throw new InvalidContactIdParameterException("id must be positive");
		}
	}
}
