package io.github.no_such_company.smailclientapp.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.nosuchcompany.pgplug.utils.PGPUtils;

import static io.github.no_such_company.smailclientapp.helper.ShaHelper.getHash;

public class MetaHandler {

    private ByteArrayInputStream inputStream;

    private User user;

    public MetaHandler(User user, String metaId, String fileId, String folder, File cachedir){
        PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
        inputStream = pgpPlugKeyHandler.fetchMailHeader(user, metaId, fileId, folder);
        this.user = user;
    }

    public String getSubjectFromMSG() {
        try {
            PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
            user.setPrivateKeyRing(pgpPlugKeyHandler.fetchPrivateKeyRingFromHost(user));
            byte[] dec = PGPUtils.decrypt(
                    inputStream,
                    user.getPrivateKeyRing(),
                    getHash(user.getKeyPass()).toCharArray());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(new String(dec));
            return node.get("subject").asText();
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
