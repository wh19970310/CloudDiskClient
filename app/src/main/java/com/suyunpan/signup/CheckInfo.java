package com.suyunpan.signup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckInfo {

    public static boolean checkPhoneNum(String str) {
        String pattern = "0?(13|14|15|17|18|19)[0-9]{9}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    public static boolean checkCode(String str) {
        return (Pattern.compile("[0-9]{6}")).matcher(str).matches();

    }

}
