package com.behl.pehchan.utility;

import java.io.File;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.behl.pehchan.dto.GeneratedKeyPairLocationDto;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

@Slf4j
public class EncryptionUtility {

	private static final String BASE_DIR_PATH = "/Users/hardikbehl/Desktop/";

	private EncryptionUtility() {
	}

	public static GeneratedKeyPairLocationDto generateKeyPair() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		final var baseFile = new File(BASE_DIR_PATH + RandomString.make(5));
		baseFile.mkdirs();
		processBuilder.directory(baseFile);
		processBuilder.command("openssl genrsa -out key.pem 2048".split(" "));
		execute(processBuilder);
		processBuilder.command("openssl rsa -in key.pem -pubout -outform DER -out public_key.der".split(" "));
		execute(processBuilder);
		processBuilder.command(
				"openssl pkcs8 -topk8 -inform PEM -outform PEM -in key.pem -out private_key.pem -nocrypt".split(" "));
		execute(processBuilder);
		return GeneratedKeyPairLocationDto.builder().basePath(baseFile.getAbsolutePath())
				.privateKey(baseFile.getAbsolutePath() + "/private_key.pem")
				.publicKey(baseFile.getAbsolutePath() + "/public_key.der").build();
	}

	public static GeneratedKeyPairLocationDto convertPemToDer(final MultipartFile privateKeyPemFile) {
		final String random = RandomString.make(5);
		final var baseFile = new File(BASE_DIR_PATH + random + "/" + privateKeyPemFile.getOriginalFilename());
		baseFile.mkdirs();
		try {
			privateKeyPemFile.transferTo(baseFile);
		} catch (final IllegalStateException | IOException exception) {
			log.error("Unable to save private file {} to {}", privateKeyPemFile, baseFile.getAbsolutePath(), exception);
			throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
		}
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.directory(new File(BASE_DIR_PATH + random));
		processBuilder.command(("openssl pkcs8 -topk8 -inform PEM -outform DER -in "
				+ privateKeyPemFile.getOriginalFilename() + " -out private_key.der -nocrypt").split(" "));
		execute(processBuilder);
		return GeneratedKeyPairLocationDto.builder().basePath(BASE_DIR_PATH + random)
				.privateKey(BASE_DIR_PATH + random + "/private_key.der").build();
	}

	private static void execute(final ProcessBuilder processBuilder) {
		try {
			final var process = processBuilder.start();
			while (process.isAlive())
				;
		} catch (final IOException exception) {
			log.error("Unable to execute process", exception);
		}
	}

}
