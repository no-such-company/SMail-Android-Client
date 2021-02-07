package io.github.no_such_company.smailclientapp.pojo.mailList;

import java.util.List;

public class MailFolder {
    private String folderName;
    private List<Mails> mails;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<Mails> getMails() {
        return mails;
    }

    public void setMails(List<Mails> mails) {
        this.mails = mails;
    }
}
