package com.behl.pehchan.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.behl.pehchan.dto.GeneratedKeyPairLocationDto;
import com.behl.pehchan.dto.UserAccountCreationRequestDto;
import com.behl.pehchan.entity.User;
import com.behl.pehchan.repository.UserRepository;
import com.behl.pehchan.security.utility.JwtUtils;
import com.behl.pehchan.utility.EncryptionUtility;
import com.behl.pehchan.utility.FileUtility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("resource")
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;

	public InputStream create(final UserAccountCreationRequestDto userAccountCreationRequestDto) {
		if (userRepository.existsByEmailId(userAccountCreationRequestDto.getEmailId()))
			throw new ResponseStatusException(HttpStatus.CONFLICT);

		final GeneratedKeyPairLocationDto generatedKeyPairLocationDto = EncryptionUtility.generateKeyPair();
		final var user = new User();
		user.setEmailId(userAccountCreationRequestDto.getEmailId());
		user.setName(userAccountCreationRequestDto.getFullName());
		try {
			user.setPublicKey(new FileInputStream(new File(generatedKeyPairLocationDto.getPublicKey())).readAllBytes());
		} catch (final IOException exception) {
			log.error("Exception occured while saving public key", exception);
			throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
		}
		final var savedUser = userRepository.save(user);
		log.info("Successfully created account for {}", savedUser.getEmailId());
		try {
			final var privateKeyResource = new FileInputStream(new File(generatedKeyPairLocationDto.getPrivateKey()));
			FileUtility.deleteDirectory(generatedKeyPairLocationDto.getBasePath());
			return privateKeyResource;
		} catch (final FileNotFoundException exception) {
			log.error("Exception occured while returning private key", exception);
			throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
		}
	}

	public String authenticate(final String emailId, final MultipartFile privateKeyFile)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		final var user = userRepository.findByEmailId(emailId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(user.getPublicKey()));
		new PKCS8EncodedKeySpec(privateKeyFile.getBytes());

		final GeneratedKeyPairLocationDto generatedKeyPairLocationDto = EncryptionUtility
				.convertPemToDer(privateKeyFile);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(
				new FileInputStream(new File(generatedKeyPairLocationDto.getPrivateKey())).readAllBytes()));

		if (!publicKey.getModulus().equals(privateKey.getModulus()) && BigInteger.valueOf(2).modPow(
				publicKey.getPublicExponent().multiply(privateKey.getPrivateExponent()).subtract(BigInteger.ONE),
				publicKey.getModulus()).equals(BigInteger.ONE))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

		return jwtUtils.generateToken(user);
	}

	public User retreiveAccountDetails(final String token) {
		return userRepository.findById(jwtUtils.extractUserId(token))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED));
	}

}
