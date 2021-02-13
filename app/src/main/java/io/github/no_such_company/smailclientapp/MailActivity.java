package io.github.no_such_company.smailclientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.github.no_such_company.smailclientapp.handler.MailSendHandler;
import io.github.no_such_company.smailclientapp.handler.PGPPlugKeyHandler;
import io.github.no_such_company.smailclientapp.handler.SharedPreferencesHandler;
import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import okhttp3.OkHttpClient;

public class MailActivity extends AppCompatActivity {

    private User user;
    private EditText recipientsInput;
    private EditText subjectInput;
    private EditText messageInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recipientsInput = findViewById(R.id.reciepients_new_mail);
        subjectInput = findViewById(R.id.subject_new_mail);
        messageInput = findViewById(R.id.new_mail_text);

        setSupportActionBar(toolbar);

        OkHttpClient client = new OkHttpClient();

        try {
            user = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e) {
            user = new SharedPreferencesHandler(this.getApplicationContext()).fetchUserObjectFromStorage();
        }
        if (user.getKeyPass() == null || user.getPasswd() == null) {
            Intent intent = new Intent(MailActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        PGPPlugKeyHandler keyHandler = new PGPPlugKeyHandler();
        user.setPublicKeyRing(keyHandler.fetchPublicKeyRingFromHost(user));
        user.setPrivateKeyRing(keyHandler.fetchPrivateKeyRingFromHost(user));

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MailSendHandler mailSendHandler = new MailSendHandler(
                        recipientsInput.getText().toString(),
                        subjectInput.getText().toString(),
                        messageInput.getText().toString(),
                        user,
                        getCacheDir()
                );

                if (mailSendHandler.isVerified()) {
                    if(!mailSendHandler.send()){
                        Toast.makeText(MailActivity.this, "There was an error. Mail not send.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MailActivity.this, "Message was send...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(MailActivity.this, "Check your recipients", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            System.out.println(id);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}