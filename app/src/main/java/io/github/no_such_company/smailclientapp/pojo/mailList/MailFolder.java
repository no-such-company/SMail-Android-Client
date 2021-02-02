package io.github.no_such_company.smailclientapp.pojo.mailList;

public class MailFolder {
    private String[] mails;
    private String folderName;

    public String[] getMails() {
        return mails;
    }

    public void setMails(String[] mails) {
        this.mails = mails;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
