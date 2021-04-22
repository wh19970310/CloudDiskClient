package com.suyunpan.my.updateinfo;

import com.apa70.idvalidation.IDValidation;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUpdateInfo {


    public static boolean checkId(String idCardNum) throws IOException {
        IDValidation idValidation = new IDValidation();
        boolean isSuccess = idValidation.validate(idCardNum);
//        Log.d("SSSS","身份证" + isSuccess);
        return isSuccess;
    }

    public static boolean checkPhone(String str) {
        String pattern = "0?(13|14|15|17|18|19)[0-9]{9}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
//        Log.d("SSSS","手机" + m.matches());
        return m.matches();
    }

    public static boolean checkCode(String str) {
//        Log.d("SSSS","验证码" + (Pattern.compile("[0-9]{6}")).matcher(str).matches());
        return (Pattern.compile("[0-9]{6}")).matcher(str).matches();

    }

    public static boolean checkPassWord(String passWord) {
        if (passWord.length() < 6 || passWord.length() > 15) {
//            Log.d("SSSS","密码NO");
            return false;
        }
//        Log.d("SSSS","密码OK ");、
        return true;
    }
}
