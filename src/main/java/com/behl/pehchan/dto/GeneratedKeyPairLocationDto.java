package com.behl.pehchan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class GeneratedKeyPairLocationDto {

	private final String basePath;
	private final String privateKey;
	private final String publicKey;

}
