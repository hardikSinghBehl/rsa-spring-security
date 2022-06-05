package com.behl.pehchan.service;

import java.security.PrivateKey;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.behl.pehchan.dto.UserAccountCreationRequestDto;
import com.behl.pehchan.entity.User;
import com.behl.pehchan.repository.UserRepository;
import com.behl.pehchan.utility.EncryptionUtility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public PrivateKey create(final UserAccountCreationRequestDto userAccountCreationRequestDto) {
		if (userRepository.existsByEmailId(userAccountCreationRequestDto.getEmailId()))
			throw new ResponseStatusException(HttpStatus.CONFLICT);

		final var keyPair = EncryptionUtility.generateKeyPair();
		final var user = new User();
		user.setEmailId(userAccountCreationRequestDto.getEmailId());
		user.setName(userAccountCreationRequestDto.getFullName());
		user.setPublicKey(keyPair.getPublic().getEncoded());
		final var savedUser = userRepository.save(user);

		log.info("Successfully created account for {}", savedUser.getEmailId());
		return keyPair.getPrivate();
	}

}
