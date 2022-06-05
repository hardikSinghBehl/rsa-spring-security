package com.behl.pehchan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.behl.pehchan.entity.User;
import com.behl.pehchan.service.UserService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/user")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<User> retreiveAccountDetail(
			@Parameter(hidden = true) @RequestHeader(name = "Authorization", required = true) final String token) {
		return ResponseEntity.ok(userService.retreiveAccountDetails(token));
	}

}
