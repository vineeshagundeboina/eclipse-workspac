package com.federal.fedmobilesmecore.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class AESCipher {



	private static final String charset = "UTF-8";
	static String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	public static String aesEncryptString(String content, String key,String iv) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		content = timeStamp + content + timeStamp;
		byte[] contentBytes = content.getBytes(charset);
		byte[] keyBytes = key.getBytes(charset);
		byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes,iv);
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(encryptedBytes);
	}

	public static String aesDecryptString(String content, String key,String iv) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Decoder decoder = Base64.getDecoder();
		byte[] encryptedBytes = decoder.decode(content);
		byte[] keyBytes = key.getBytes(charset);
		byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes,iv);
		return new String(decryptedBytes, charset);
	}

	public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes,String iv) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE, iv);
	}

	public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes,String iv) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE,iv);
	}

	private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode,String iv)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
		byte[] initParam = iv.getBytes(charset);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(mode, secretKey, ivParameterSpec);

		return cipher.doFinal(contentBytes);
	}

}
