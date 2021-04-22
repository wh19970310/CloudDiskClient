package com.suyunpan.appdata;

import com.suyunpan.filesystem.FileInfo;
import com.suyunpan.utils.model.User;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    public final static String URL = "172.16.27.242";
    public static List<FileInfo> downLoadFileInfoList = new ArrayList<>();
    public static List<FileInfo> localFileInfoList = new ArrayList<>();
    public static List<FileInfo> upLoadFileInfoList = new ArrayList<>();
    public static String cloudCurrentDirectory = "";
    public static String localPath = "/storage/emulated/0";
    public static User user;
}
