package com.centura.contact.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.centura.contact.domain.ContactDigest2;
import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.model.Contact;
import com.centura.contact.repository.ContactRepository;

@Service
public class ContactServiceImpl2 implements ContactService2 {
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public List<ContactDigest2> findAll(Integer startPage, Integer pageSize) {
		Pageable pageable = PageRequest.of(startPage, pageSize);
		List<Contact> contacts = contactRepository.findAll(pageable).toList();
		List<ContactDigest2> results = new ArrayList<>();

		if (contacts != null) {
			for (Contact contact : contacts) {
				results.add(buildContactDigest(contact));
			}
		}

		return results;
	}

	@Override
	public ContactDigest2 findById(Long id) throws ContactNotFoundException {
		Optional<Contact> contact = contactRepository.findById(id);

		if (contact.isPresent()) {
			return buildContactDigest(contact.get());
		}

		throw new ContactNotFoundException("unable to find " + id);
	}

	private ContactDigest2 buildContactDigest(Contact contact) {
		ContactDigest2 digest = new ContactDigest2();

		digest.setId(contact.getId());
		digest.setFirstName(contact.getFirstName());
		digest.setLastName(contact.getLastName());
		digest.setPhone(contact.getPhone());

		return digest;
	}

}
