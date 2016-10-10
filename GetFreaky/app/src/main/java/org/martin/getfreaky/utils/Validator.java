package org.martin.getfreaky.utils;

/**
 * Created by martin on 2016. 10. 09..
 */

public class Validator {
    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }
}
