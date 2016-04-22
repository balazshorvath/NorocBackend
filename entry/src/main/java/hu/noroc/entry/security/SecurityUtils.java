package hu.noroc.entry.security;

import hu.noroc.entry.network.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;
import java.util.Random;

/**
 *
 * Code from: https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
 * @author JavaDigest
 * Created by Oryk on 4/7/2016.
 */
public class SecurityUtils {
    public static String randomString(int length){
        final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in {session}.key and {session}.key files.
     */
    public static void generateKey(Client client) throws NoSuchAlgorithmException {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair key = keyGen.generateKeyPair();
            client.setKey(key);
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *          : original plain text
     * @param key
     *          :The public key
     * @return Encrypted text
     */
    public static String encrypt(String text, PublicKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance("RSA");
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return new String(Base64.getEncoder().encode(cipher.doFinal(text.getBytes())));
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *          :encrypted text
     * @param key
     *          :The private key
     * @return plain text
     */
    public static String decrypt(String text, PrivateKey key) {
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] buffer = Base64.getDecoder().decode(text.getBytes());
            StringBuilder sb = new StringBuilder();
            int i;
            for(i = 0; ((i + 1) * 128) < buffer.length; i++) {
                sb.append(new String(cipher.doFinal(buffer, i * 128, (i + 1) * 128)));
            }
            if((i * 128) < buffer.length){
                sb.append(new String(cipher.doFinal(buffer, i * 128, buffer.length)));
            }
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
