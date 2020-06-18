package com.centura.contact.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContactDigest2 {
	@ApiModelProperty(notes = "Contact Id")
	private Long id;

	@ApiModelProperty(notes = "First Name")
	private String firstName;

	@ApiModelProperty(notes = "Last Name")
	private String lastName;

	@ApiModelProperty(notes = "Cell Phone")
	private String phone;
}
