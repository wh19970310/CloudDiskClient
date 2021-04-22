package com.suyunpan.utils;

import com.suyunpan.appdata.AppData;

public class TrimChar {
    public String trimFirstAndLastChar() {
        String element = "/";
        String[] strs = AppData.cloudCurrentDirectory.split("/");
        AppData.cloudCurrentDirectory = "";
        for (int i = 0; i < strs.length - 1; i++) {
            AppData.cloudCurrentDirectory = AppData.cloudCurrentDirectory + "/" + strs[i];
        }
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        String tmp = null;
        while (beginIndexFlag || endIndexFlag) {
            int beginIndex = AppData.cloudCurrentDirectory.indexOf(element) == 0 ? 1 : 0;
            int endIndex = AppData.cloudCurrentDirectory.lastIndexOf(element) + 1 == AppData.cloudCurrentDirectory.length() ? AppData.cloudCurrentDirectory.lastIndexOf(element) : AppData.cloudCurrentDirectory.length();
            try {
                tmp = AppData.cloudCurrentDirectory.substring(beginIndex, endIndex);
            } catch (Exception e) {
                return "/" + AppData.cloudCurrentDirectory;
            }
            AppData.cloudCurrentDirectory = tmp;
            beginIndexFlag = (AppData.cloudCurrentDirectory.indexOf(element) == 0);
            endIndexFlag = (AppData.cloudCurrentDirectory.lastIndexOf(element) + 1 == AppData.cloudCurrentDirectory.length());
        }
        return "/" + AppData.cloudCurrentDirectory;
    }
}
