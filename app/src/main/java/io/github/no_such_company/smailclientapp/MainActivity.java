package io.github.no_such_company.smailclientapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.github.no_such_company.smailclientapp.helper.AlternateHostHelper.getFinalDestinationHost;
import static io.github.no_such_company.smailclientapp.helper.TypeHelper.stringToBool;

public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        try {
            user = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e) {

        }
        if (user == null) {
            user = fetchUserFromStorage();
        }
        if (user == null || user.getKeyPass() == null || user.getPasswd() == null || user.getAddress() == null || !checkCredentials(user.getAddress(), user.getPasswd())) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, MailsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private User fetchUserFromStorage() {
        User user = new User();
        SharedPreferences sharedPreferences = getSharedPreferences("SMailClient", MODE_PRIVATE);
        user.setAddress(sharedPreferences.getString("user", null));
        user.setPasswd(sharedPreferences.getString("pw", null));
        user.setKeyPass(sharedPreferences.getString("kp", null));
        user.setSwitch1(stringToBool(sharedPreferences.getString("sw1", "false")));
        user.setSwitch2(stringToBool(sharedPreferences.getString("sw2", "false")));
        user.setSwitch3(stringToBool(sharedPreferences.getString("sw3", "false")));

        return user;
    }

    private boolean checkCredentials(String userName, String pass) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", userName)
                .addFormDataPart("hash", pass)
                .build();

        try {
            Request request = null;
            request = new Request.Builder()
                    .url(getFinalDestinationHost(userName.split("//:")[0]) + "/inbox/mails")
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}