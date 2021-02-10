package io.github.no_such_company.smailclientapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.no_such_company.smailclientapp.adapter.MailBoxRecyclerViewAdapter;
import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.mailList.MailBox;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.github.no_such_company.smailclientapp.helper.AlternateHostHelper.getFinalDestinationHost;

public class MailActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OkHttpClient client = new OkHttpClient();

        try{
            user = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e){
            user = fetchUserFromStorage();
        }
        if(user.getKeyPass() == null || user.getPasswd() == null){
            Intent intent = new Intent(MailActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

    private User fetchUserFromStorage(){
        User user = new User();
        SharedPreferences sharedPreferences = getSharedPreferences("SMail_client", MODE_PRIVATE);
        user.setAddress(sharedPreferences.getString("user", null));
        user.setPasswd(sharedPreferences.getString("pw", null));
        return user;
    }
}