package cn.rxframework.utils.security;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {
    private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);

    public static final String KEY_ALGORITHM = "RSA";

    /**
     * Default key length. The value shall be in the range of 512-65536.
     */
    private static final int KEY_LENGTH = 1024;

    public static final String PUBLIC_KEY = "PublicKey";

    public static final String PRIVATE_KEY = "PrivateKey";

    /**
     * Algorithm to be used for signature and verification.
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * Generate a RSA key pair with public key and private key.
     *
     * @param seed SecureRandom seed
     * @return keyMap, the public key is indicated by "PublicKey" and private key is indicated by "PrivateKey".
     */
    public static Map<String, Object> generateKeyPair(String seed) {
        try {
            Map<String, Object> keyMap = new HashMap<>();

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(seed.getBytes());
            keyPairGenerator.initialize(KEY_LENGTH, secureRandom);

            KeyPair keyPair = keyPairGenerator.genKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            keyMap.put(PUBLIC_KEY, publicKey.getEncoded());
            keyMap.put(PRIVATE_KEY, privateKey.getEncoded());

            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * Generate a RSA key pair with public key and private key.
     * Use current datetime as random seed.
     *
     * @return keyMap, the public key is indicated by "PublicKey" and private key is indicated by "PrivateKey".
     */
    public static Map<String, Object> generateKeyPair() {
        String currentDateTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        return generateKeyPair(currentDateTime);
    }

    /**
     * Save the keys into the key file.
     *
     * @param keyPair            key pair to be saved into the file.
     * @param publicKeyFileName  file to save public key.
     * @param privateKeyFileName file to save private key.
     */
    public static void saveKeyPair(Map<String, Object> keyPair, String publicKeyFileName, String privateKeyFileName) {
        //Write public key into the key file.
        try {
            byte[] publicKeyByte = (byte[]) keyPair.get(PUBLIC_KEY);
            FileUtils.writeByteArrayToFile(new File(publicKeyFileName), publicKeyByte);
        } catch (FileNotFoundException ex) {
            logger.error("", ex);
        } catch (IOException ex) {
            logger.error("", ex);
        }

        //Write private key into the key file.
        try {
            byte[] privateKeyByte = (byte[]) keyPair.get(PRIVATE_KEY);
            FileUtils.writeByteArrayToFile(new File(privateKeyFileName), privateKeyByte);
        } catch (FileNotFoundException ex) {
            logger.error("", ex);
        } catch (IOException ex) {
            logger.error("", ex);
        }
    }

    /**
     * Get the key from local file.
     *
     * @param keyFileName file containing the key.
     * @return byte array containing the key.
     */
    public static byte[] getKey(String keyFileName) {
        byte[] keyBytes = null;
        //Get key from file.
        try {
            keyBytes = FileUtils.readFileToByteArray(new File(keyFileName));
        } catch (FileNotFoundException ex) {
            logger.error("", ex);
        } catch (IOException ex) {
            logger.error("", ex);
        }

        return keyBytes;
    }

    /**
     * Encrypt the data with the public key.
     *
     * @param data           data to be encrypted.
     * @param offset         offset of data to be encrypted.
     * @param length         length of data to be encrypted.
     * @param publicKeyBytes public key in binary format.
     * @return encrypted data.
     */
    public static byte[] encryptWithPublicKey(byte[] data, int offset, int length, byte[] publicKeyBytes) {

        byte[] encryptedData = null;

        try {
            //Create a new X509EncodedKeySpec with the given encoded key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);

            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            //Get the public key from the provided key specification.
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            //Init the ciper.
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

            //Encrypt mode!
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //Do the encryption now !!!!
            encryptedData = cipher.doFinal(data, offset, length);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error("", ex);
        }

        return encryptedData;
    }

    /**
     * Encrypt the data with private key.
     *
     * @param data            data to be encrypted.
     * @param offset          offset of data to be encrypted.
     * @param length          length of data to be encrypted.
     * @param privateKeyBytes private key in binary format.
     * @return encrypted data.
     */
    public static byte[] encryptWithPrivateKey(byte[] data, int offset, int length, byte[] privateKeyBytes) {
        byte[] encryptedData = null;

        try {
            // Create a new PKCS8EncodedKeySpec with the given encoded key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            //Get the private key from the provided key specification.
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            //Init the ciper.
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

            //Encrypt mode!
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            //Do the encryption now !!!!
            encryptedData = cipher.doFinal(data, offset, length);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error("", ex);
        }

        return encryptedData;
    }

    /**
     * Decrypt data with public key.
     *
     * @param data           data to be decrypted.
     * @param offset         offset of data to be decrypted.
     * @param length         length of data to be decrypted.
     * @param publicKeyBytes public key in binary format.
     * @return decrypted data.
     */
    public static byte[] decryptWithPublicKey(byte[] data, int offset, int length, byte[] publicKeyBytes) {

        byte[] encryptedData = null;

        try {
            //Create a new X509EncodedKeySpec with the given encoded key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);

            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            //Get the public key from the provided key specification.
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            //Init the ciper.
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

            //Decrypt mode!
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            //Do the decryption now !!!!
            encryptedData = cipher.doFinal(data, offset, length);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error("", ex);
        }

        return encryptedData;
    }

    /**
     * Decrypt the data with private key bytes.
     *
     * @param data            data to be decrypted.
     * @param offset          offset of data to be decrypted.
     * @param length          length of data to be decrypted.
     * @param privateKeyBytes private key in binary format.
     * @return decrypted data.
     */
    public static byte[] decryptWithPrivateKey(byte[] data, int offset, int length, byte[] privateKeyBytes) {
        byte[] encryptedData = null;

        try {
            // Create a new PKCS8EncodedKeySpec with the given encoded key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            //Get the private key from the provided key specification.
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            //Init the ciper.
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

            //Decrypt mode!
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //Do the decryption now !!!!
            encryptedData = cipher.doFinal(data, offset, length);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error("", ex);
        }

        return encryptedData;
    }

    /**
     * Decrypt the data with private key.
     *
     * @param data       data to be decrypted.
     * @param offset     offset of data to be decrypted.
     * @param length     length of data to be decrypted.
     * @param privateKey private key.
     * @return decrypted data.
     */
    public static byte[] decryptWithPrivateKey(byte[] data, int offset, int length, PrivateKey privateKey) {
        byte[] encryptedData = null;

        try {
            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);


            //Init the ciper.
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());

            //Decrypt mode!
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            //Do the decryption now !!!!
            encryptedData = cipher.doFinal(data, offset, length);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            logger.error("", ex);
        }

        return encryptedData;
    }

    /**
     * Sign the data with the private key.
     *
     * @param data            data to be signed.
     * @param offset          offset of data to be signed.
     * @param length          length of data to be signed.
     * @param privateKeyBytes private key in binary format.
     * @return signed data.
     */
    public static byte[] sign(byte[] data, int offset, int length, byte[] privateKeyBytes) {
        byte[] signedData = null;
        try {
            // Create a new PKCS8EncodedKeySpec with the given encoded key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            //Get the private key from the provided key specification.
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            //Create the Signature instance with RSA MD5.
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

            //Use private key to do the signature.
            signature.initSign(privateKey);

            //Updates the data to be signed or verified, using the specified array of bytes.
            signature.update(data, offset, length);

            //Sign it now.
            signedData = signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException ex) {
            logger.error("", ex);
        }

        return signedData;
    }

    /**
     * Verify the signature.
     *
     * @param data           data signed.
     * @param offset         offset of data to be verified.
     * @param length         length of data to be verified.
     * @param publicKeyBytes public key in binary format.
     * @param dataSignature  signature for the data.
     * @return Whether the signature is fine.
     */
    public static boolean verify(byte[] data, int offset, int length, byte[] publicKeyBytes, byte[] dataSignature) {
        boolean result = false;
        try {
            //Create a new X509EncodedKeySpec with the given encoded key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);

            //RSA key factory.
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            //Get the public key from the provided key specification.
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            //Create the Signature instance with RSA MD5.
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

            //Use pubic key to verify the signature.
            signature.initVerify(publicKey);

            //Updates the data to be signed or verified, using the specified array of bytes.
            signature.update(data, offset, length);

            //Verifies the passed-in signature.
            result = signature.verify(dataSignature);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | InvalidKeyException ex) {
            logger.error("", ex);
        }

        return result;
    }
}
