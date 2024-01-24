package zet.kedzieri.bank.config;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.*;

@Component
public class SensitiveDataCipher {

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public SensitiveDataCipher() {
        PublicKey publicKey;
        PrivateKey privateKey;
        try {
            System.err.println("Loading public key...");
            publicKey = deserializePublicKey(System.getenv("RSA_PUBLIC_KEY"));
            System.err.println("Public key loaded!");

            System.err.println("Loading private key...");
            privateKey = deserializePrivateKey(System.getenv("RSA_PRIVATE_KEY"));
            System.err.println("Private key loaded!");

            System.err.println("Testing encryption and decryption capabilities...");
            String test = "test";
            if (!test.equals(decryptText(privateKey, encryptText(publicKey, test)))) {
                throw new RuntimeException();
            }
            System.err.println("Encryption is working!");
        } catch (Exception ignored) {
            publicKey = null;
            privateKey = null;
            System.err.println("Encryption is NOT working!");
        }
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String encrypt(String plainText) {
        if (publicKey == null || privateKey == null) {
            throw new CipherUninitializedException();
        }
        try {
            String plainTextPlusNonce = plainText + UUID.randomUUID();
            return Base64.getEncoder().encodeToString(encryptText(publicKey, plainTextPlusNonce));
        } catch (Exception e) {
            throw new CipherFailureException(e);
        }
    }

    public String decrypt(String cipherText) {
        if (publicKey == null || privateKey == null) {
            throw new CipherUninitializedException();
        }
        try {
            String plainTextPlusNonce = decryptText(privateKey, Base64.getDecoder().decode(cipherText));
            return plainTextPlusNonce.substring(0, plainTextPlusNonce.length() - UUID.randomUUID().toString().length());
        } catch (Exception e) {
            throw new CipherFailureException(e);
        }
    }

    private static final String KEY_DERIVATION_ALGORITHM = "RSA";
    private static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";

    private static final KeyFactory KEY_FACTORY = getKeyFactory();

    public static String serializePublicKey(PublicKey publicKey) {
        byte[] encodedKey = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public static PublicKey deserializePublicKey(String serializedPublicKey) {
        serializedPublicKey = serializedPublicKey
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");
        try {
            byte[] encodedKey = Base64.getDecoder().decode(serializedPublicKey);
            return KEY_FACTORY.generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String serializePrivateKey(PrivateKey privateKey) {
        byte[] encodedKey = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    public static PrivateKey deserializePrivateKey(String serializedPrivateKey) {
        serializedPrivateKey = serializedPrivateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        try {
            byte[] encodedKey = Base64.getDecoder().decode(serializedPrivateKey);
            return KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptText(PublicKey publicKey, String plainText) {
        return encrypt(publicKey, plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException |
                 NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptText(PrivateKey privateKey, byte[] encryptedText) {
        return new String(decrypt(privateKey, encryptedText), StandardCharsets.UTF_8);
    }

    private static KeyFactory getKeyFactory() {
        try {
            return KeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static class CipherUninitializedException extends RuntimeException {

        public CipherUninitializedException() {
            super("Szyfrowanie/Deszyfrowanie nie działa. Prawdopodobnie błąd podczas wczytywania kluczy.");
        }

    }

    public static class CipherFailureException extends RuntimeException {

        public CipherFailureException(Exception e) {
            super("Szyfrowanie/Deszyfrowanie tych danych się nie powiodło", e);
        }

    }

}
