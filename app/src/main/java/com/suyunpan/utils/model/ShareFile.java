package com.suyunpan.utils.model;

public class ShareFile {

    private String sharelink;

    private String md5;

    private String sharecode;

    private Integer belonguser;

    private String filename;

    public String getSharelink() {
        return sharelink;
    }

    public void setSharelink(String sharelink) {
        this.sharelink = sharelink;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSharecode() {
        return sharecode;
    }

    public void setSharecode(String sharecode) {
        this.sharecode = sharecode;
    }

    public Integer getBelonguser() {
        return belonguser;
    }

    public void setBelonguser(Integer belonguser) {
        this.belonguser = belonguser;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
