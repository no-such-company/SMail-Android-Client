package io.github.no_such_company.smailclientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.no_such_company.smailclientapp.handler.SharedPreferencesHandler;
import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.mailList.MailBox;

import io.github.no_such_company.smailclientapp.pojo.mailList.MailFolder;
import io.github.no_such_company.smailclientapp.pojo.mailList.Mails;
import moe.leer.tree2view.TreeView;
import moe.leer.tree2view.module.DefaultTreeNode;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.github.no_such_company.smailclientapp.helper.AlternateHostHelper.getFinalDestinationHost;

public class MailsActivity extends AppCompatActivity {

    private User user;

    private MailBox mailBox;

    private TreeView treeView;

    private DefaultTreeNode<String> root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mail_folder_list);

        OkHttpClient client = new OkHttpClient();

        try{
            user = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e){
            user = new SharedPreferencesHandler(this.getApplicationContext()).fetchUserObjectFromStorage();
        }
        if(user.getKeyPass() == null || user.getPasswd() == null){
            Intent intent = new Intent(MailsActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MailsActivity.this, MailActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", user.getAddress())
                .addFormDataPart("hash", user.getPasswd())
                .build();
        try{
        Request request = new Request.Builder()
                .url(getFinalDestinationHost(user.getAddress().split("//:")[0]) + "/inbox/mails")
                .post(requestBody)
                .build();

            Response response = client.newCall(request).execute();
            ObjectMapper objectMapper = new ObjectMapper();
            mailBox = objectMapper.readValue(response.body().string(), MailBox.class);

            root = new DefaultTreeNode<String>(user.getAddress());

            for(MailFolder folders : mailBox.getFolder()){
                DefaultTreeNode<String> folder = new DefaultTreeNode<String>(folders.getFolderName());
                for(Mails mail :folders.getMails()) {
                    folder.addChild(new DefaultTreeNode<String>(mail.getMailId()));
                }
                root.addChild(folder);
            }

            treeView = (TreeView) findViewById(R.id.tree_view);
            treeView.setRoot(root);
            treeView.setDefaultAnimation(true);
            treeView.requestLayout();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}