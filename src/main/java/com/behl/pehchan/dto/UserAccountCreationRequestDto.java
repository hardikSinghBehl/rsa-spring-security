package com.behl.pehchan.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class UserAccountCreationRequestDto {

	@Email
	@Schema(example = "hardik.behl7444@gmail.com")
	private final String emailId;
	@NotNull
	@Schema(example = "Hardik Singh Behl")
	private final String fullName;

}
