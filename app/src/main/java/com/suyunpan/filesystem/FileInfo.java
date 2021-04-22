package com.suyunpan.filesystem;

public class FileInfo {
    private String fileName;
    private String fileSize = "-1";
    private int imgId;
    private String fileOperation = "";
    private String fileTransferRate = "";
    private String fileMD5 = "";
    private Long sizeInByte;
    private String filePosition = "";
    private boolean isCloudOperation = false;

    public boolean isCloudOperation() {
        return isCloudOperation;
    }

    public void setCloudOperation(boolean cloudOperation) {
        isCloudOperation = cloudOperation;
    }

    public String getFilePosition() {
        return filePosition;
    }

    public void setFilePosition(String filePosition) {
        this.filePosition = filePosition;
    }

    public Long getSizeInByte() {
        return sizeInByte;
    }

    public void setSizeInByte(Long sizeInByte) {
        this.sizeInByte = sizeInByte;
    }

    public String toString() {

        return fileName + "---" + fileSize + "---" + fileOperation + "---" + fileMD5;
    }

    public String getFileMD5() {
        return fileMD5;
    }

    public void setFileMD5(String fileMD5) {
        this.fileMD5 = fileMD5;
    }

    public String getFileOperation() {
        return fileOperation;
    }

    public void setFileOperation(String fileOperation) {
        this.fileOperation = fileOperation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getFileTransferRate() {
        return fileTransferRate;
    }

    public void setFileTransferRate(String fileTransferRate) {
        this.fileTransferRate = fileTransferRate;
    }
}
