package com.behl.pehchan.controller;

import java.io.ByteArrayInputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.pehchan.dto.UserAccountCreationRequestDto;
import com.behl.pehchan.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final UserService userService;

	@PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<InputStreamResource> create(
			@RequestBody(required = true) final UserAccountCreationRequestDto userAccountCreationRequestDto) {
		final var privateKey = userService.create(userAccountCreationRequestDto);
		final var objectContent = new InputStreamResource(new ByteArrayInputStream(privateKey.getEncoded()));
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=private.der").body(objectContent);
	}

}
