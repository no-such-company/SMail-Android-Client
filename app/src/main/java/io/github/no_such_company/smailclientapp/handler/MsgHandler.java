package io.github.no_such_company.smailclientapp.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import io.github.no_such_company.smailclientapp.bytestream.ByteArrayInOutStream;
import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.mail.MailObject;
import io.github.nosuchcompany.pgplug.sign.SignedFileProcessor;
import io.github.nosuchcompany.pgplug.utils.PGPUtils;

import static io.github.no_such_company.smailclientapp.helper.ShaHelper.getHash;

public class MsgHandler {

    private ByteArrayInputStream inputStream;

    private User user;

    private String sender;

    private JsonNode node;

    private MailObject mailObject;

    public MsgHandler(User user, String metaId, String fileId, String folder, String sender) {
        PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
        inputStream = pgpPlugKeyHandler.fetchMailHeader(user, metaId, fileId, folder);
        this.user = user;
        this.sender = sender;
        getMsgFromServer();
    }

    public MailObject build(){
        mailObject = new MailObject();

        setContent();
        setSender();
        setSubject();

        return mailObject;
    }

    public void setSubject(){
        mailObject.setSubject(node.get("subject").asText());
    }

    public void setContent(){
        mailObject.setContent(node.get("content").asText());
    }

    public void setSender(){
        mailObject.setSender(node.get("sender").asText());
    }

    private String getMsgFromServer() {
        try {
            PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
            user.setPrivateKeyRing(pgpPlugKeyHandler.fetchPrivateKeyRingFromHost(user));
            InputStream pubKey = pgpPlugKeyHandler.fetchPublicKeyRingFromHost(sender);

            ByteArrayInOutStream designed = (ByteArrayInOutStream) SignedFileProcessor.verifyFile(inputStream, pubKey);

            byte[] dec = PGPUtils.decrypt(
                    designed.getInputStream(),
                    user.getPrivateKeyRing(),
                    getHash(user.getKeyPass()).toCharArray());

            ObjectMapper mapper = new ObjectMapper();
            node = mapper.readTree(new String(dec));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
