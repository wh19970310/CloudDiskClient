package com.suyunpan.filesystem;

import com.suyunpan.R;

import java.util.List;

public class SetFileSizeIcon {

    public static void setFileIc(List<FileInfo> fileInfoList) {

        String extension = "";

        for (FileInfo fileInfo : fileInfoList) {
            if (fileInfo.getFileSize().equals("-1")) {
                fileInfo.setFileSize("");
                fileInfo.setImgId(R.drawable.ic_folder);
            } else {
                fileInfo.setSizeInByte(Long.valueOf(fileInfo.getFileSize()));
                fileInfo.setFileSize(FileSizeChange.sizeChange(Long.valueOf(fileInfo.getFileSize())));

                try {
                    extension = "";
                    extension = fileInfo.getFileName().substring(fileInfo.getFileName().lastIndexOf(".") + 1).trim();
                    extension = extension.toLowerCase();
                } catch (Exception e) {
                }

                if (extension.equals("jpg") || extension.equals("gif") || extension.equals("bmp")
                        || extension.equals("png") || extension.equals("jpeg")) {
                    fileInfo.setImgId(R.drawable.ic_photo);
                } else if (extension.equals("mp4") || extension.equals("wmv") || extension.equals("avi")
                        || extension.equals("rmvb") || extension.equals("mkv") || extension.equals("mov")) {
                    fileInfo.setImgId(R.drawable.ic_movie);
                } else if (extension.equals("apk")) {
                    fileInfo.setImgId(R.drawable.ic_apk);
                } else if (extension.equals("exe")) {
                    fileInfo.setImgId(R.drawable.ic_exe);
                } else if (extension.equals("docx") || extension.equals("doc") || extension.equals("xls")
                        || extension.equals("xlsx") || extension.equals("ppt") || extension.equals("pptx")) {
                    fileInfo.setImgId(R.drawable.ic_office);
                } else if (extension.equals("txt") || extension.equals("text")) {
                    fileInfo.setImgId(R.drawable.ic_text);
                } else if (extension.equals("")) {
                    fileInfo.setImgId(R.drawable.ic_unknow);
                } else {
                    fileInfo.setImgId(R.drawable.ic_file);
                }
            }
        }
    }
}
