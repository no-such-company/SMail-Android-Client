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

    public MailObject() {
    }

    ;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
