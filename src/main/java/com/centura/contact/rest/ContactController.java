package com.centura.contact.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.centura.contact.domain.ContactDigest;
import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.exception.InvalidContactIdParameterException;
import com.centura.contact.service.ContactService;

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

	@GetMapping("/contact/{id} ")
	public ContactDigest getContactById(@PathVariable("id") Long id) throws ContactNotFoundException, InvalidContactIdParameterException {
		log.info("/contact/" + id);

		validateId(id);

		return contactService.findById(id);
	}

	private void validateId(Long id) throws InvalidContactIdParameterException {
		if (id == null) {
			throw new InvalidContactIdParameterException("missing id");
		}
		if (id < 0) {
			throw new InvalidContactIdParameterException("id must be positive");
		}
	}
}
