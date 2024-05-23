package com.example.encryption.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionService {

    public SecretKey generateKey(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    public String encrypt(String algorithm, SecretKey key, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data);
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public SecretKey getKeyFromString(String algorithm, String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
    }
}
