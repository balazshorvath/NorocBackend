package hu.noroc.entry.security;

import hu.noroc.entry.network.Client;

import javax.crypto.Cipher;
import java.security.*;

/**
 *
 * Code from: https://javadigest.wordpress.com/2012/08/26/rsa-encryption-example/
 * @author JavaDigest
 * Created by Oryk on 4/7/2016.
 */
public class SecurityUtils {
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
        public static byte[] encrypt(String text, PublicKey key) {
            byte[] cipherText = null;
            try {
                // get an RSA cipher object and print the provider
                final Cipher cipher = Cipher.getInstance("RSA");
                // encrypt the plain text using the public key
                cipher.init(Cipher.ENCRYPT_MODE, key);
                cipherText = cipher.doFinal(text.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return cipherText;
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
        public static String decrypt(byte[] text, PrivateKey key) {
            try {
                // get an RSA cipher object and print the provider
                final Cipher cipher = Cipher.getInstance("RSA");

                // decrypt the text using the private key
                cipher.init(Cipher.DECRYPT_MODE, key);

                return new String(cipher.doFinal(text));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
}
