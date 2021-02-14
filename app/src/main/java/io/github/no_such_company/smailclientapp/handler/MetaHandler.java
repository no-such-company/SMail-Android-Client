package io.github.no_such_company.smailclientapp.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.nosuchcompany.pgplug.utils.PGPUtils;

import static io.github.no_such_company.smailclientapp.helper.ShaHelper.getHash;

public class MetaHandler {

    private ByteArrayInputStream inputStream;

    private User user;

    private String mailId;

    private File cacheDir;

    public MetaHandler(User user, String metaId, String fileId, String folder, File cachedir){
        PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
        inputStream = pgpPlugKeyHandler.fetchMailHeader(user, metaId, fileId, folder);
        this.user = user;
        this.mailId = metaId;
        this.cacheDir = cachedir;
    }

    public String getSubjectFromMSG() {
        try {
            byte[] dec = PGPUtils.decrypt(
                    inputStream,
                    user.getPrivateKeyRing(),
                    getHash(user.getKeyPass()).toCharArray());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(dec.toString());
            return node.get("subject").asText();
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}
