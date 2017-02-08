package cn.rxframework.utils.security;

import cn.rxframework.utils.common.ByteUtils;
import cn.rxframework.utils.security.RSAUtils;

import java.util.Map;

public class RSAUtilsTest {

    public static void main(String[] args) {
        testDecryption();
//        testDecryption1();
//        testDecryption2();
//        testSignature();
    }

    private static void testDecryption() {
        try {
            Map<String, Object> keyPair = RSAUtils.generateKeyPair("as67f67sad");

            String path = "/Users/xule/Downloads/temp/";
            String pukFile = path + "puk.txt";
            String prkFile = path + "prk.txt";
            RSAUtils.saveKeyPair(keyPair, pukFile, prkFile);

            //Get public key and private key from the result.
            byte[] pubicKeyBytes = RSAUtils.getKey(pukFile);//(byte[]) keyPair.get(RSAUtils.PUBLIC_KEY);
            byte[] privateKeyBytes = (byte[]) keyPair.get(RSAUtils.PRIVATE_KEY);

            //Encrypt the test data with public key then decrypt it with private key.
            String inputString = "Hi, 测试非对称加密％……＃$％……＊";
            byte[] dataEncryptedWithPublicKey = RSAUtils.encryptWithPublicKey(inputString.getBytes(), 0, inputString.getBytes().length, pubicKeyBytes);
            //Decrypt it with private key
            byte[] dataDecryptedWithPrivateKey = RSAUtils.decryptWithPrivateKey(dataEncryptedWithPublicKey, 0, dataEncryptedWithPublicKey.length, privateKeyBytes);
            System.out.println(new String(dataDecryptedWithPrivateKey));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void testDecryption1() {
        try {
            Map<String, Object> keyPair = RSAUtils.generateKeyPair();

            //Get public key and private key from the result.
            byte[] pubicKeyBytes = (byte[]) keyPair.get(RSAUtils.PUBLIC_KEY);
            byte[] privateKeyBytes = (byte[]) keyPair.get(RSAUtils.PRIVATE_KEY);
            System.out.println(String.format("pubicKeyBytes : %s", ByteUtils.Bytes2HexString(pubicKeyBytes)));
            pubicKeyBytes = ByteUtils.HexString2Bytes(ByteUtils.Bytes2HexString(pubicKeyBytes));
            System.out.println(String.format("privateKeyBytes : %s", ByteUtils.Bytes2HexString(privateKeyBytes)));
            privateKeyBytes = ByteUtils.HexString2Bytes(ByteUtils.Bytes2HexString(privateKeyBytes));

            //Encrypt the test data with public key then decrypt it with private key.
            String inputString = "Hi, this is neil's test data. Don't try to play tricks with me.";
            byte[] dataEncryptedWithPublicKey = RSAUtils.encryptWithPublicKey(inputString.getBytes(), 0, inputString.getBytes().length, pubicKeyBytes);
            byte[] dataDecryptedWithPrivateKey = RSAUtils.decryptWithPrivateKey(dataEncryptedWithPublicKey, 0, dataEncryptedWithPublicKey.length, privateKeyBytes);
            String outputString = new String(dataDecryptedWithPrivateKey);
            System.out.println(outputString);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void testDecryption2() {
        try {
            //Create RSA key pair.
            Map<String, Object> keyPair = RSAUtils.generateKeyPair();

            //Get public key and private key from the result.
            byte[] publicKeyBytes = (byte[]) keyPair.get(RSAUtils.PUBLIC_KEY);
            byte[] privateKeyBytes = (byte[]) keyPair.get(RSAUtils.PRIVATE_KEY);

            //Encrypt the test data with private key then decrypt it with public key.
            String inputString = "Hi, this is neil's test data. Don't try to play tricks with me.";
            byte[] dataEncryptedWithPrivateKey = RSAUtils.encryptWithPrivateKey(inputString.getBytes(), 0, inputString.getBytes().length, privateKeyBytes);
            byte[] dataDecryptedWithPublicKey = RSAUtils.decryptWithPublicKey(dataEncryptedWithPrivateKey, 0, dataEncryptedWithPrivateKey.length, publicKeyBytes);
            String outputString = new String(dataDecryptedWithPublicKey);
            System.out.println(outputString);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void testSignature() {
        try {
            //Create RSA key pair.
            Map<String, Object> keyPair = RSAUtils.generateKeyPair();

            //Get public key and private key from the resut.
            byte[] publicKeyBytes = (byte[]) keyPair.get(RSAUtils.PUBLIC_KEY);
            byte[] privateKeyBytes = (byte[]) keyPair.get(RSAUtils.PRIVATE_KEY);

            //Generate the signature then verify the result.
            String inputString = "Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.Hi, this is neil's test data. Don't try to play tricks with me.";
            byte[] dataSignature = RSAUtils.sign(inputString.getBytes(), 0, inputString.getBytes().length, privateKeyBytes);
            boolean result = RSAUtils.verify(inputString.getBytes(), 0, inputString.getBytes().length, publicKeyBytes, dataSignature);

            System.out.println(result);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
