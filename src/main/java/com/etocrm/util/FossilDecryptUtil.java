package com.etocrm.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class FossilDecryptUtil {

    final int ITERATOR_COUNT = 1000;
    final int KEY_LENGTH = 128;
    final String DELIMITER = "]";
    final int SALT_LENGTH = 8;
    final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
    final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    public String decrypt(String ciphertext, String password)
            throws Exception {
        String[] fields = ciphertext.split(DELIMITER);
        if (fields.length != 3) {
            throw new IllegalArgumentException("Invalid encypted text format");
        }
        try {
            byte[] salt = Base64.getDecoder().decode(fields[0]);
            byte[] iv = Base64.getDecoder().decode(fields[1]);
            byte[] cipherBytes = Base64.getDecoder().decode(fields[2]);

            SecretKey key = getKey(salt, password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            GCMParameterSpec ivParams = new GCMParameterSpec(KEY_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            byte[] plaintext = cipher.doFinal(cipherBytes);
            String plainrStr = new String(plaintext, "UTF-8");

            return plainrStr;
        } catch (Throwable e) {
            throw new Exception("Error while decryption", e);
        }
    }

    SecretKey getKey(byte[] salt, String password)
            throws Exception {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    ITERATOR_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(
                    KEY_DERIVATION_ALGORITHM);
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Throwable e) {
            throw new Exception("Error while generating key", e);
        }
    }



}
