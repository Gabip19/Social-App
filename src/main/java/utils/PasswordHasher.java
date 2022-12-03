package utils;

import domain.HashedPasswordDTO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

    public static String getHashedPassword(String passwordToHash, String salt) {
        byte[] saltByte = Base64.getDecoder().decode(salt.getBytes());
        byte[] hashedPassword = getHashedPasswordAux(passwordToHash, saltByte);
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    public static HashedPasswordDTO newHashForPassword(String passwordToHash) {
        byte[] salt = getNewSalt();
        byte[] hashedPassword = getHashedPasswordAux(passwordToHash, salt);

        String saltAsString = Base64.getEncoder().encodeToString(salt);
        String hashedPasswordAsString = Base64.getEncoder().encodeToString(hashedPassword);

        return new HashedPasswordDTO(saltAsString, hashedPasswordAsString);
    }

    private static byte[] getHashedPasswordAux(String passwordToHash, byte[] salt) {
        byte[] hashedPassword;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            hashedPassword = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return hashedPassword;
    }

    private static byte[] getNewSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }
}
