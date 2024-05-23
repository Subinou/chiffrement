package com.example.encryption.controller;

import com.example.encryption.service.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/encryption")
public class EncryptionController {

    @Autowired
    private EncryptionService encryptionService;

    @PostMapping("/encrypt")
    public ResponseEntity<?> encryptFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("algorithm") String algorithm,
        @RequestParam("keySize") int keySize
    ) {
        try {
            SecretKey secretKey = encryptionService.generateKey(algorithm, keySize);

            byte[] fileData = file.getBytes();
            String encryptedData = encryptionService.encrypt(algorithm, secretKey, fileData);

            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            Path filePath = Paths.get(System.getProperty("java.io.tmpdir"), file.getOriginalFilename() + ".enc");
            Files.write(filePath, Base64.getDecoder().decode(encryptedData));

            return ResponseEntity.ok(new EncryptionResponse(encodedKey, filePath.toString()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors du chiffrement du fichier: " + e.getMessage());
        }
    }

    static class EncryptionResponse {
        private String key;
        private String filePath;

        public EncryptionResponse(String key, String filePath) {
            this.key = key;
            this.filePath = filePath;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
