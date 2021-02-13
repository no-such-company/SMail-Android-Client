package io.github.no_such_company.smailclientapp.handler;

import java.io.ByteArrayInputStream;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.nosuchcompany.pgplug.utils.PGPUtils;

public class MetaHandler {

    private ByteArrayInputStream inputStream;

    private User user;

    public MetaHandler(User user, String metaId, String fileId){
        PGPPlugKeyHandler pgpPlugKeyHandler = new PGPPlugKeyHandler();
        inputStream = pgpPlugKeyHandler.fetchMailHeader(user, metaId, fileId);
        this.user = user;
    }

    public String getRecipent(){
        PGPUtils.decrypt(
                inputStream,
                user.getPrivateKeyRing(),
                decrypt_outputStream,
                user.getKeyPass());
    }


}
