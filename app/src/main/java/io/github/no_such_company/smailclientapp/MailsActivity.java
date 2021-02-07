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

public class MailsActivity extends AppCompatActivity implements MailBoxRecyclerViewAdapter.ItemClickListener {

    private User user;

    MailBoxRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mail_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OkHttpClient client = new OkHttpClient();

        try{
            user = (User) getIntent().getSerializableExtra("user");
        } catch (Exception e){
            user = fetchUserFromStorage();
        }
        if(user.getKeyPass() == null || user.getPasswd() == null){
            Intent intent = new Intent(MailsActivity.this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        RecyclerView recyclerView = findViewById(R.id.folderNamesRecyle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
            MailBox mailBox = objectMapper.readValue(response.body().string(), MailBox.class);
            adapter = new MailBoxRecyclerViewAdapter(this, mailBox.getFolder());
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e){

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
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

    private User fetchUserFromStorage(){
        User user = new User();
        SharedPreferences sharedPreferences = getSharedPreferences("SMail_client", MODE_PRIVATE);
        user.setAddress(sharedPreferences.getString("user", null));
        user.setPasswd(sharedPreferences.getString("pw", null));
        return user;
    }
}