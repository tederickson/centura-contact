package com.centura.contact.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.centura.contact.domain.ContactDigest;
import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.model.Contact;
import com.centura.contact.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public List<ContactDigest> findAll() {
		List<Contact> contacts = contactRepository.findAll();
		List<ContactDigest> results = new ArrayList<>();

		if (contacts != null) {
			for (Contact contact : contacts) {
				results.add(buildContactDigest(contact));
			}
		}

		return results;
	}

	@Override
	public ContactDigest findById(Long id) throws ContactNotFoundException {
		Optional<Contact> contact = contactRepository.findById(id);

		if (contact.isPresent()) {
			return buildContactDigest(contact.get());
		}

		throw new ContactNotFoundException("unable to find " + id);
	}

	private ContactDigest buildContactDigest(Contact contact) {
		ContactDigest digest = new ContactDigest();

		digest.setId(contact.getId());
		digest.setName(contact.getName());
		digest.setPhone(contact.getPhone());

		return digest;
	}

}
