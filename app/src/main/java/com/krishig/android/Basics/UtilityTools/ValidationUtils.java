package com.krishig.android.Basics.UtilityTools;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("You can't create instance of Util class. Please use as static..");
    }

    /**
     * This method validates whether input is
     * only number and latter.
     */
    public static boolean isNumberLetter(String data) {
        String expr = "^[A-Za-z0-9]+$";
        return data.matches(expr);
    }

    /**
     * This method validates whether input is
     * only number.
     */
    public static boolean isNumber(String data) {
        String expr = "^[0-9]+$";
        return data.matches(expr);
    }

    /**
     * This method validates whether input is
     * only latter.
     */
    public static boolean isLetter(String data) {
        String expr = "^[A-Za-z]+$";
        return data.matches(expr);
    }

    /**
     * This method validates whether input is
     * valid account number or not.
     */
    public static boolean isValidAccountNumber(String data) {
        String expr = "^\\d{9,18}$";
        return data.matches(expr);
    }

    /**
     * This method validates whether input is
     * valid ifsc code or not.
     */
    public static boolean isValidIFSC(String data) {
        String expr = "^[A-Z]{4}0[A-Z0-9]{6}$";
        return data.matches(expr);
    }

    /**
     * This method validates whether input name is valid or not.
     */
    public static boolean isValidName(String name) {
        if (isLetter(name)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * A username is considered valid if all the following condition are satisfied :
     * <p>
     * Username consists of alphanumeric characters (a-zA-Z0-9), lowercase, or uppercase.
     * Username allowed of the dot (.), underscore (_), and hyphen (-).
     * The dot (.), underscore (_), or hyphen (-) must not be the first or last character.
     * The dot (.), underscore (_), or hyphen (-) does not appear consecutively, e.g., java..regex
     * The number of characters must be between 6 to 20.
     * <p>
     * Explain : Regular Expression.
     * <p>
     * regex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){6,20}[a-zA-Z0-9]$";
     * <p>
     * Valid Username :
     * "mkyong"
     * "javaregex"
     * "JAVAregex"
     * "java.regex"
     * "java-regex"
     * "java_regex"
     * "java.regex.123"
     * "java-regex-123"
     * "java_regex_123"
     * "javaregex123"
     * "123456"
     * "java123"
     * "01234567890123456789"
     * <p>
     * Invalid Username :
     * "abc"                      invalid length 3, length must between 6-20
     * "01234567890123456789a"    invalid length 21, length must between 6-20
     * "_javaregex_"              invalid start and last character
     * ".javaregex."              invalid start and last character
     * "-javaregex-"              invalid start and last character
     * "javaregex#$%@123"         invalid symbols, support dot, hyphen and underscore
     * "java..regex"              dot cant appear consecutively
     * "java--regex"              hyphen can't appear consecutively
     * "java__regex"              underscore can't appear consecutively
     * "java._regex"              dot and underscore can't appear consecutively
     * "java.-regex"              dot and hyphen can't appear consecutively
     * " ",                       empty
     * "",                        empty
     */
    public static boolean isValidUsernameFormat(String username) {
        Pattern pattern;
        Matcher matcher;
        final String USERNAME_PATTERN = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){6,20}[a-zA-Z0-9]$";
        pattern = Pattern.compile(USERNAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static int isValidUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return 1;
        } else if (username.length() < 6) {
            return 2;
        } else if (username.length() > 20) {
            return 3;
        } else if (!isValidUsernameFormat(username)) {
            return 4;
        }
        return 0;
    }

    /**
     * A password is considered valid if all the following constraints are satisfied :
     * <p>
     * It contains at least 8 characters and at most 20 characters.
     * It contains at least one digit.
     * It contains at least one upper case alphabet.
     * It contains at least one lower case alphabet.
     * It contains at least one special character which includes !@#$%&*()-+=^.
     * It dose not contain any white space.
     * <p>
     * Example :
     * “Geeks@portal20”    = true      // This password satisfies all constraints mentioned above.
     * “Geeksforgeeks”     = false     // It contains upper case and lower case alphabet but doesn’t contains any digits, and special characters.
     * “Geeks@ portal9”    = false     // It contains upper case alphabet, lower case alphabet, special characters, digits along with white space which is not valid.
     * <p>
     * Explain : Regular Expression.
     * <p>
     * regex = “^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8, 20}$”
     * <p>
     * where :
     * ^ represents starting character of the string.
     * (?=.*[0-9]) represents a digit must occur at least once.
     * (?=.*[a-z]) represents a lower case alphabet must occur at least once.
     * (?=.*[A-Z]) represents an upper case alphabet that must occur at least once.
     * (?=.*[@#$%^&-+=()] represents a special character that must occur at least once.
     * (?=\\S+$) white spaces don’t allowed in the entire string.
     * .{8, 20} represents at least 8 characters and at most 20 characters.
     * $ represents the end of the string.
     * <p>
     * https://www.techiedelight.com/validate-password-java/
     */
    public static int isValidPassword(String password) {

        /* represents an upper case alphabet that must occur at least once. */
        boolean hasUppercase = !password.matches(".*[A-Z].*");

        /* represents a lower case alphabet must occur at least once. */
        boolean hasLowercase = !password.matches(".*[a-z].*");

        /* represents a digit must occur at least once. */
        boolean hasNumber = !password.matches(".*\\d+.*");

        /* represents a special character that must occur at least once. */
        boolean hasSpecialCharacter = !password.matches(".*[!@#$%&*()-+=^].*");

        /* white spaces don’t allowed in the entire string. */
        boolean hasWhiteSpaces = password.matches(".*[ ].*");

        if (StringUtils.isBlank(password)) {
            return 1;
        } else if (password.length() < 8) {
            return 2;
        } else if (password.length() > 20) {
            return 3;
        } else if (hasUppercase) {
            return 4;
        } else if (hasLowercase) {
            return 5;
        } else if (hasNumber) {
            return 6;
        } else if (hasSpecialCharacter) {
            return 7;
        } else if (hasWhiteSpaces) {
            return 8;
        }
        return 0;
    }

    /**
     * This method validates whether input confirm password is valid or not.
     */
    public static int isValidConfirmPassword(String password, String confirmPassword) {
        if (StringUtils.isBlank(password)) {
            return 1;
        } else if (StringUtils.isBlank(confirmPassword)) {
            return 2;
        } else if (confirmPassword.length() < 8) {
            return 3;
        } else if (!confirmPassword.equals(password)) {
            return 4;
        }
        return 0;
    }

    /**
     * This method validates whether input email is valid or not.
     */
    public static boolean isValidEmailFormat(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return 1;
        } else if (!isValidEmailFormat(email)) {
            return 2;
        }
        return 0;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (!StringUtils.isBlank(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    /**
     * This method validates whether input phone number is valid or not.
     */
    public static int isPhoneNumberValid(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return 1;
        } else {
            Pattern pattern;
            Matcher matcher;
            final String TEN_DIGIT_PATTERN = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
            pattern = Pattern.compile(TEN_DIGIT_PATTERN);
            matcher = pattern.matcher(phoneNumber);
            if (!matcher.matches()) {
                return 2;
            } else {
                return 0;
            }

        }
    }

    public static int isPinCodeValid(String pincode) {
        if (StringUtils.isEmpty(pincode)) {
            return 1;
        } else {
            Pattern pattern;
            Matcher matcher;
            final String TEN_DIGIT_PATTERN = "^[0-9]{6}$";
            pattern = Pattern.compile(TEN_DIGIT_PATTERN);
            matcher = pattern.matcher(pincode);
            if (!matcher.matches()) {
                return 2;
            } else {
                return 0;
            }
        }
    }
}
