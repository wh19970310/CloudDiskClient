package com.suyunpan.login;

public class LoginInfo {

    private static String userId;
    private static String pwd;
    private static String response;

    public static String getResponse() {
        return response;
    }

    public static void setResponse(String response) {
        LoginInfo.response = response;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        LoginInfo.userId = userId;
    }

    public static String getPwd() {
        return pwd;
    }

    public static void setPwd(String pwd) {
        LoginInfo.pwd = pwd;
    }
}
