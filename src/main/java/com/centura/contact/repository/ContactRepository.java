package com.centura.contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.centura.contact.model.Contact;

// see https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html
public interface ContactRepository extends JpaRepository<Contact, Long> {

}
