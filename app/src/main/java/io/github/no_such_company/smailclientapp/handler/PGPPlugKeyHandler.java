package io.github.no_such_company.smailclientapp.handler;

import org.bouncycastle.openpgp.PGPPublicKey;

import java.io.ByteArrayInputStream;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.github.no_such_company.smailclientapp.helper.AddressHelper.getHostFromAddress;
import static io.github.no_such_company.smailclientapp.helper.AlternateHostHelper.getFinalDestinationHost;
import static io.github.nosuchcompany.pgplug.utils.PGPUtils.readPublicKey;

public class PGPPlugKeyHandler {

    public PGPPublicKey fetchPublicKeyRingFromHost(User user) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user.getAddress())
                .build();

        try {
            Request request = null;
            request = new Request.Builder()
                    .url(getFinalDestinationHost(user.getHost()) + "/in/pubkey")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            return readPublicKey(response.body().bytes());
        } catch (Exception e) {
            return null;
        }
    }

    public ByteArrayInputStream fetchPrivateKeyRingFromHost(User user) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user.getAddress())
                .addFormDataPart("hash", user.getPasswd())
                .build();

        try {
            Request request = null;
            request = new Request.Builder()
                    .url(getFinalDestinationHost(user.getHost()) + "/inbox/privkey")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            return new ByteArrayInputStream(response.body().bytes());
        } catch (Exception e) {
            return null;
        }
    }

    public PGPPublicKey fetchRecipientsPublicKeyRingFromHost(String user) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user)
                .build();

        try {
            Request request = null;
            request = new Request.Builder()
                    .url(getFinalDestinationHost(getHostFromAddress(user)) + "/in/pubkey")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            return readPublicKey(response.body().bytes());
        } catch (Exception e) {
            return null;
        }
    }

    public ByteArrayInputStream fetchMailHeader(User user, String metaId, String fileId, String folder){
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user.getAddress())
                .addFormDataPart("hash", user.getPasswd())
                .addFormDataPart("mailId", metaId)
                .addFormDataPart("fileId", fileId)
                .addFormDataPart("folder", folder)
                .build();

        try {
            Request request = null;
            request = new Request.Builder()
                    .url(getFinalDestinationHost(user.getHost()) + "/inbox/mail/file")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            return new ByteArrayInputStream(response.body().bytes());
        } catch (Exception e) {
            return null;
        }
    }
}
