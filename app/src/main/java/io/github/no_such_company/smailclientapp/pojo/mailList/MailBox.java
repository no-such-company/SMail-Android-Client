package io.github.no_such_company.smailclientapp.pojo.mailList;

import java.util.List;

public class MailBox {
    private List<MailFolder> folder;

    public List<MailFolder> getFolder() {
        return folder;
    }

    public void setFolder(List<MailFolder> folder) {
        this.folder = folder;
    }
}
