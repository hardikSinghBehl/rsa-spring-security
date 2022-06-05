package com.behl.pehchan.controller;

import java.util.HashMap;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

	@GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> healthCheck() {
		final var response = new HashMap<String, String>();
		response.put("message", "pong");
		return ResponseEntity.ok(response);
	}

}
