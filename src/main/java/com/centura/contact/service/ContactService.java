package com.centura.contact.service;

import java.util.List;

import com.centura.contact.domain.ContactDigest;
import com.centura.contact.exception.ContactNotFoundException;

public interface ContactService {
	List<ContactDigest> findAll();

	ContactDigest findById(Long id) throws ContactNotFoundException;

}
