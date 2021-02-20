package io.github.no_such_company.smailclientapp.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.meta.InBoxMetaData;
import io.github.nosuchcompany.pgplug.utils.PGPUtils;

import static io.github.no_such_company.smailclientapp.helper.ShaHelper.getHash;

public class MetaHandler {

    private ByteArrayInputStream inputStream;

    private User user;

    private JsonNode node;

    private InBoxMetaData metaObject;

    public MetaHandler(User user, String metaId, String fileId, String folder) {
        PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
        inputStream = pgpPlugKeyHandler.fetchMailHeader(user, metaId, fileId, folder);
        this.user = user;
        getMsgFromServer();
    }

    public InBoxMetaData build(){
        metaObject = new InBoxMetaData();

        setTimeCode();
        setSender();
        setMailId();

        return metaObject;
    }

    public void setSender(){
        metaObject.setSender(node.get("sender").asText());
    }

    public void setTimeCode(){
        metaObject.setTimecode(node.get("timeCode").asLong());
    }

    public void setMailId(){
        metaObject.setMailId(node.get("mailId").asText());
    }

    private String getMsgFromServer() {
        try {
            PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
            user.setPrivateKeyRing(pgpPlugKeyHandler.fetchPrivateKeyRingFromHost(user));

            byte[] dec = PGPUtils.decrypt(
                    inputStream,
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
