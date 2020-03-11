package cn.rxframework.utils.mail.bean;

import java.io.File;

public class MessageFile {
    private String originName;
    private File referFile;

    public MessageFile(String originName, File referFile) {
        this.originName = originName;
        this.referFile = referFile;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public File getReferFile() {
        return referFile;
    }

    public void setReferFile(File referFile) {
        this.referFile = referFile;
    }
}
