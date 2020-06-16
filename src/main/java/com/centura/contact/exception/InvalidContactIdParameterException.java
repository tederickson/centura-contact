package com.centura.contact.exception;

/**
 * @deprecated Use IllegalArgumentException instead of creating a new Exception.
 */
@Deprecated
public class InvalidContactIdParameterException extends Exception {

	private static final long serialVersionUID = -1117193118888293838L;

	public InvalidContactIdParameterException(String message) {
		super(message);
	}

}
