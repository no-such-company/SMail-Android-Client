package io.github.no_such_company.smailclientapp.pojo.mailList;

public class Mails {
    private String mailId;
    private String[] attachments;
    private String[] mailDescriptors;

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    public String[] getMailDescriptors() {
        return mailDescriptors;
    }

    public void setMailDescriptors(String[] mailDescriptors) {
        this.mailDescriptors = mailDescriptors;
    }
}
