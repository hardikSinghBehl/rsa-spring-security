package com.behl.pehchan.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.behl.pehchan.dto.UserAccountCreationRequestDto;
import com.behl.pehchan.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final UserService userService;

	@PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<InputStreamResource> create(
			@RequestBody(required = true) final UserAccountCreationRequestDto userAccountCreationRequestDto) {
		final var privateKey = userService.create(userAccountCreationRequestDto);
		final var objectContent = new InputStreamResource(privateKey);
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=private_key.pem").body(objectContent);
	}

	@PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<Map<String, String>> login(
			@RequestPart(name = "privateKey", required = true) final MultipartFile privateKeyFile,
			@RequestPart(name = "emailId", required = true) final String emailId) {
		try {
			return ResponseEntity.ok(Map.of("JWT", userService.authenticate(emailId, privateKeyFile)));
		} catch (final NoSuchAlgorithmException | InvalidKeySpecException | IOException exception) {
			log.error("Exception occurred while validating key pair", exception);
			throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
		}
	}

}
