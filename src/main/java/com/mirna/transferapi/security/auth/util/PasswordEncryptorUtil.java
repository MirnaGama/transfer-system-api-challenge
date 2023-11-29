package com.mirna.transferapi.security.auth.util;

import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordEncryptorUtil {
	 
	private static StrongPasswordEncryptor encryptor = new StrongPasswordEncryptor();
	
	public static String encryptPassword(String password) {
		return encryptor.encryptPassword(password);
	}
	
	public static boolean checkPassword(String password, String encryptedPassword) {
		return encryptor.checkPassword(password, encryptedPassword);
	}
}
