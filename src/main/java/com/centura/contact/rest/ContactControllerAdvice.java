package com.centura.contact.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.centura.contact.exception.ContactNotFoundException;
import com.centura.contact.exception.InvalidContactIdParameterException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ContactControllerAdvice {

	@ResponseBody
	@ExceptionHandler(ContactNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String serviceExceptionHandler(ContactNotFoundException ex) {
		log.warn("contact not found", ex);
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(InvalidContactIdParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String serviceExceptionHandler(InvalidContactIdParameterException ex) {
		log.warn("invalid Id parameter", ex);
		return ex.getMessage();
	}
}
