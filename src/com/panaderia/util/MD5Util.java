package com.panaderia.util;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class MD5Util {

	private static final char[] CONSTS_HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final String iv = "0123456789123456";
	private static final String secretKey = "c0n4l3pMD5";

	public static String encriptaMD5(String stringAEncriptar) {
		try {
			MessageDigest msgd = MessageDigest.getInstance("MD5");
			byte[] bytes = msgd.digest(stringAEncriptar.getBytes());
			StringBuilder strbCadenaMD5 = new StringBuilder(2 * bytes.length);
			for (int i = 0; i < bytes.length; i++) {
				int bajo = (int) (bytes[i] & 0x0f);
				int alto = (int) ((bytes[i] & 0xf0) >> 4);
				strbCadenaMD5.append(CONSTS_HEX[alto]);
				strbCadenaMD5.append(CONSTS_HEX[bajo]);
			}
			return strbCadenaMD5.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	private static String md5(final String input) throws NoSuchAlgorithmException {
		final MessageDigest md = MessageDigest.getInstance("MD5");
		final byte[] messageDigest = md.digest(input.getBytes());
		final BigInteger number = new BigInteger(1, messageDigest);
		return String.format("%032x", number);
	}

	private static Cipher initCipher(final int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		final SecretKeySpec skeySpec = new SecretKeySpec(md5(secretKey).getBytes(), "AES");
		final IvParameterSpec initialVector = new IvParameterSpec(iv.getBytes());
		final Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
		cipher.init(mode, skeySpec, initialVector);
		return cipher;
	}

	public static String encrypt(final String dataToEncrypt) {
		String encryptedData = null;
		try {
			// Initialize the cipher
			final Cipher cipher = initCipher(Cipher.ENCRYPT_MODE);
			// Encrypt the data
			final byte[] encryptedByteArray = cipher.doFinal(dataToEncrypt.getBytes());
			// Encode using Base64
			//encryptedData = (new BASE64Encoder()).encode(encryptedByteArray);
			encryptedData = Base64.encodeBase64String(encryptedByteArray);
			
			
		} catch (Exception e) {
			System.err.println("Problem encrypting the data");
			e.printStackTrace();
		}
		return encryptedData;
	}

	public static String decrypt(final String encryptedData) {
		String decryptedData = null;
		try {
			// Initialize the cipher
			final Cipher cipher = initCipher(Cipher.DECRYPT_MODE);
			// Decode using Base64
			//final byte[] encryptedByteArray = (new BASE64Decoder()).decodeBuffer(encryptedData);
			final byte[] encryptedByteArray = Base64.decodeBase64(encryptedData);
			// Decrypt the data
			final byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
			decryptedData = new String(decryptedByteArray, "UTF8");
		} catch (Exception e) {
			System.err.println("Problem decrypting the data");
			e.printStackTrace();
		}
		return decryptedData;
	}
}