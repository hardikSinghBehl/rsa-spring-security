package com.behl.pehchan.utility;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtility {

	public static void deleteDirectory(final String directoryPath) {
		final var processBuilder = new ProcessBuilder();
		processBuilder.command("rm", "-rf", directoryPath);
		try {
			processBuilder.start();
		} catch (final IOException exception) {
			log.error("Unable to delete directory {}", directoryPath);
		}
	}

}
