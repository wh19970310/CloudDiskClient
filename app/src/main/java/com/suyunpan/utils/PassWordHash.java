package com.suyunpan.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PassWordHash {

    private String passWord;

    public String sha1() throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(passWord.getBytes());
        StringBuffer buf = new StringBuffer();
        byte[] bits = md.digest();
        for (int i = 0; i < bits.length; i++) {
            int a = bits[i];
            if (a < 0) a += 256;
            if (a < 16) buf.append("0");
            buf.append(Integer.toHexString(a));
        }
        return buf.toString();
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

}
