package hu.noroc.entry;

import hu.noroc.entry.security.Compressor;
import hu.noroc.entry.security.SecurityUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Oryk on 4/6/2016.
 */
//TODO: I think these can be easily tested, and should be
public class NetworkData {
    public static String compressAndRSAData(String data, PublicKey key) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        String result = Compressor.gzip(data);
        return SecurityUtils.encrypt(result, key);
    }
    public static String rsaData(String data, PublicKey key) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        return SecurityUtils.encrypt(data, key);
    }
    public static String decompressAndRSADecryptData(String data, PrivateKey key) {
        String result = Compressor.gunzip(data);
        return SecurityUtils.decrypt(result, key);
    }
    public static String rsaDecryptData(String data, PrivateKey key) {
        return SecurityUtils.decrypt(data, key);
    }

//
//    /**
//     * @param session 32 bytes of random things
//     * @return
//     */
//    public static SecretKeySpec generateInitialKey(String session) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        byte[] hash = MessageDigest.getInstance("SHA-256").digest(session.getBytes("UTF-8"));
//        int now = Calendar.getInstance().get(Calendar.HOUR);
//        byte[] key = new byte[256];
//        for (int i = 0, x = 0; i < 265; i++) {
//            key[i] = (byte) (hash[x++] + (now - x));
//            if(x == 32) x = 0;
//        }
//        return new SecretKeySpec(key, "AES");
//    }
//
//    private static byte[] aes(byte[] data, SecretKeySpec key){
//        try {
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            return cipher.doFinal(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static SecretKeySpec generateAESKey() throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(256);
//        // Generate the secret key specs.
//        SecretKey skey = keyGenerator.generateKey();
//        return new SecretKeySpec(skey.getEncoded(), "AES");
//    }
}
