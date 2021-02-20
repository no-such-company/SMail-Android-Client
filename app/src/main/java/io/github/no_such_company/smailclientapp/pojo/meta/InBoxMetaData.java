package io.github.no_such_company.smailclientapp.pojo.meta;

public class InBoxMetaData {

    private Long timecode;
    private String sender;
    private String mailId;

    public InBoxMetaData(Long timecode, String sender, String mailId) {
        this.timecode = timecode;
        this.sender = sender;
        this.mailId = mailId;
    }

    public InBoxMetaData() {
    }

    public Long getTimecode() {
        return timecode;
    }

    public void setTimecode(Long timecode) {
        this.timecode = timecode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }
}
