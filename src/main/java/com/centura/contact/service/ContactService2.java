package com.centura.contact.service;

import java.util.List;

import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.exception.ContactNotFoundException;

public interface ContactService2 {
	List<ContactDigest2> findAll();

	ContactDigest2 findById(Long id) throws ContactNotFoundException;
}
