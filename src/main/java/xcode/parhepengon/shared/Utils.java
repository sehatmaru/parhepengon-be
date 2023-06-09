package xcode.parhepengon.shared;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {

    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour
    private static final long TEMPORARY_EXPIRE_DURATION = 5 * 60 * 1000; // 5 minute
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+~`|}{[]\\:;?><,./-=";

    public static String generateSecureId() {
        return UUID.randomUUID().toString();
    }

    public static String generateSecureCharId() {
        StringBuilder sb = new StringBuilder(20);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 20; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String generateSecurePassword() {
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(randomIndex));
        }

        return password.toString();
    }

    public static String encryptor(String value, boolean isEncrypt) {
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();

        jasypt.setPassword("xcode");

        return isEncrypt ? jasypt.encrypt(value) :jasypt.decrypt(value);
    }

    public static String generateOTP() {
        int length = 5;

        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        Random random = new Random();
        int otp = random.nextInt(max - min + 1) + min;

        return String.valueOf(otp);
    }

    public static long getDifferenceDays(Date date1, Date date2) {
        long diff = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Date setDateTime(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);

        if (hour == 7) {
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        } else {
            cal.set(Calendar.MINUTE, 58);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        }

        return cal.getTime();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String[] stringToArray(String requests) {
        return requests.split(",");
    }

    public static Date getTomorrowDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_DURATION);
    }

    public static Date getTemporaryDate() {
        return new Date(System.currentTimeMillis() + TEMPORARY_EXPIRE_DURATION);
    }

    public static String mask(String value) {
        return isEmail(value) ? maskEmail(value) : maskUsername(value);
    }
    public static String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex > 0) {
            String domain = email.substring(atIndex);
            String username = email.substring(0, atIndex);
            return maskCharacters(username) + domain;
        } else {
            return email;
        }
    }

    public static String maskUsername(String username) {
        return maskCharacters(username);
    }

    private static String maskCharacters(String input) {
        int length = input.length();
        int maskLength = Math.max(1, length / 2); // Mask half of the input
        int startIndex = (length - maskLength) / 2;
        int endIndex = startIndex + maskLength;
        StringBuilder masked = new StringBuilder(input);
        for (int i = startIndex; i < endIndex; i++) {
            masked.setCharAt(i, '*');
        }
        return masked.toString();
    }

    public static boolean isEmail(String input) {
        // Regular expression pattern for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);

        return pattern.matcher(input).matches();
    }
}
