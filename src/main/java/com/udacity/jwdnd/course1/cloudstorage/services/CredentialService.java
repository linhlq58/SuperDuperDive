package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public int createNewCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        return credentialMapper.insert(
                new Credential(
                        null,
                        credential.getUrl(),
                        credential.getUserName(),
                        encodedKey,
                        encryptedPassword,
                        userService.getCurrentUserId()
                )
        );
    }

    public int updateCredential(Credential newCredential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(newCredential.getPassword(), encodedKey);

        newCredential.setKey(encodedKey);
        newCredential.setPassword(encryptedPassword);

        return credentialMapper.update(newCredential);
    }

    public List<Credential> getAllCredentials() {
        return credentialMapper.getAllCredentials(userService.getCurrentUserId());
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public void deleteCredential(Integer credentialId) {
        credentialMapper.deleteCredential(credentialId);
    }

    public String decryptedPassword(String encryptedPassword, String key) {
        return encryptionService.decryptValue(encryptedPassword, key);
    }
}
