package io.github.no_such_company.smailclientapp.pojo.mail;

public class MailObject {
    private String subject;
    private String content;
    private String sender;

    public MailObject(String subject, String content, String sender) {
        this.subject = subject;
        this.content = content;
        this.sender = sender;
    }
}
