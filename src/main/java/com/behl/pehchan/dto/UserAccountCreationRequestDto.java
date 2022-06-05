package com.behl.pehchan.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserAccountCreationRequestDto {

	@Email
	private final String emailId;
	@NotNull
	private final String fullName;

}
