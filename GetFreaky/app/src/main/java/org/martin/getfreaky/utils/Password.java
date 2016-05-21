package org.martin.getfreaky.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/**
 * Created by martin on 2016. 05. 21..
 * <p/>
 * This class provides static methods to generate secure password hash using MD5 with salt,
 * and a method to verify password with existing hash and salt
 */
public class Password {

    public static String getHash(String password) {
        byte[] salt = new byte[0];
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return getSecurePassword(password, salt);
    }

    public static boolean equals(String password, String storedPassword) {
        String[] passAndSalt = storedPassword.split(":");
        String salt = passAndSalt[1];
        byte[] saltInBytes = new byte[0];
        try {
            saltInBytes = fromHex(salt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return storedPassword.equals(getSecurePassword(password, saltInBytes));
    }

    private static String getSecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();

            String saltString = toHex(salt);

            // Append the salt at the end of the hash
            generatedPassword = generatedPassword + ":" + saltString;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = new SecureRandom();
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
