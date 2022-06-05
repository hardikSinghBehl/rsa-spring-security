package com.behl.pehchan.utility;

import java.security.KeyPair;

import io.jsonwebtoken.impl.crypto.RsaProvider;

public class EncryptionUtility {

	private EncryptionUtility() {
	}

	public static KeyPair generateKeyPair() {
		return RsaProvider.generateKeyPair();
	}

}
