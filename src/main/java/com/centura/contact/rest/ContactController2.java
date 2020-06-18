package com.centura.contact.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.service.ContactService2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for version 2 of the API.
 */

@RestController
@Slf4j
@Api(value = "contactV2", description = "Version 2 of the Contact List API")
public class ContactController2 {
	@Autowired
	private ContactService2 contactService;

	@ApiOperation(value = "Retrieve a Contact")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "Successfully retrieved Contact"), //
			@ApiResponse(code = 400, message = "Invalid Contact Id"), //
			@ApiResponse(code = 404, message = "Contact Id not found") })
	@GetMapping("/v2/contact/{id}")
	public ContactDigest2 getContactById(@PathVariable("id") Long id) throws ContactNotFoundException {
		log.info("/v2/contact/" + id);

		if (id < 0) {
			throw new IllegalArgumentException("Id must be positive");
		}

		return contactService.findById(id);
	}

	@ApiOperation(value = "Retrieve a list of all Contacts", //
			notes = "An empty list is returned if there are no entries for the requested page.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 404, message = "Will not happen, an empty list is returned.") })
	@GetMapping("/v2/contacts")
	public List<ContactDigest2> getContacts(@RequestParam(value = "start", defaultValue = "0") Integer startPage,
			@RequestParam(value = "size", defaultValue = "40") Integer pageSize) {
		log.info("/v2/contacts?start=" + startPage + "&size=" + pageSize);

		return contactService.findAll(startPage, pageSize);
	}

}
