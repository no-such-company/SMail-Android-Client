package io.github.no_such_company.smailclientapp.handler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.mail.MailObject;

import io.github.nosuchcompany.pgplug.sign.SignedFileProcessor;
import io.github.nosuchcompany.pgplug.utils.PGPUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.github.no_such_company.smailclientapp.helper.AlternateHostHelper.getFinalDestinationHost;


public class MailSendHandler {
    public static final String TEMP_SMAIL_MSG = "msg";

    private List<String> recipients;
    private String subject;
    private String message;
    private User user;
    private PGPPlugKeyHandler pgpPlugKeyHandler;
    private File cacheDir;
    OkHttpClient client;

    public MailSendHandler(String recipients, String subject, String message, User user, File cacheDir) {
        this.recipients = Arrays.asList(recipients.split(";"));
        this.subject = subject;
        this.message = message;
        this.user = user;
        this.pgpPlugKeyHandler = new PGPPlugKeyHandler();
        this.cacheDir = cacheDir;
        client = new OkHttpClient();
    }

    public boolean isVerified() {
        for (String recipient : recipients) {
            if (pgpPlugKeyHandler.fetchRecipientsPublicKeyRingFromHost(recipient) == null) {
                return false;
            }
        }
        return true;
    }

    public boolean send() {
        try {
            doSend(recipients, new MailObject(subject, message, user.getAddress()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void doSend(List<String> recipientList, MailObject mailObject) throws NoSuchAlgorithmException, PGPException, SignatureException, NoSuchProviderException, IOException {
        File attachment = createAttachment(mailObject);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user.getAddress())
                .addFormDataPart("pwhash", user.getPasswd())
                .addFormDataPart("recipients", getToString(recipientList.toArray(new String[recipientList.size()])))
                .addFormDataPart("file", attachment.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), attachment))
                .build();

        try {
            Request request = null;
            request = new Request.Builder()
                    .url(getFinalDestinationHost(user.getHost()) + "/inbox/mail/send")
                    .post(requestBody)
                    .build();
            client.newCall(request).execute();

            cleanDir(cacheDir,getDirSize(cacheDir));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createAttachment(MailObject mailObject) throws IOException, PGPException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(mailObject);

        File file = new File(cacheDir, TEMP_SMAIL_MSG);
        OutputStream outputStream = new FileOutputStream(file);

        Set<PGPPublicKey> publicKeys = new HashSet<PGPPublicKey>();
        for (String recipient : recipients) {
            publicKeys.add(pgpPlugKeyHandler.fetchRecipientsPublicKeyRingFromHost(recipient));
        }

        PGPUtils.encrypt(outputStream, json.getBytes(), publicKeys);
        SignedFileProcessor.signFile(
                TEMP_SMAIL_MSG,
                user.getPrivateKeyRing(),
                new FileOutputStream(file),
                user.getKeyPass().toCharArray(),
                true
        );
        return new File(cacheDir, TEMP_SMAIL_MSG);
    }

    public String getToString(String[] arrayData) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arrayData.length; i++) {
            stringBuilder.append(arrayData[i]);
            if (i < arrayData.length - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    private static void cleanDir(File dir, long bytes) {

        long bytesDeleted = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            bytesDeleted += file.length();
            file.delete();

            if (bytesDeleted >= bytes) {
                break;
            }
        }
    }

    private static long getDirSize(File dir) {

        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }
        }

        return size;
    }
}
