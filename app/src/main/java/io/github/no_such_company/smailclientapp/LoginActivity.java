package io.github.no_such_company.smailclientapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import io.github.no_such_company.smailclientapp.handler.PGPPlugKeyHandler;
import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.mailList.MailBox;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static io.github.no_such_company.smailclientapp.helper.AlternateHostHelper.getFinalDestinationHost;
import static io.github.no_such_company.smailclientapp.helper.TypeHelper.boolToString;

public class LoginActivity extends AppCompatActivity {

    EditText editTextTextPersonName;
    EditText editTextTextPassword;
    EditText editTextTextPassword2;
    Switch  switch1, switch2, switch3;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        client = new OkHttpClient();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        final User[] user = {(User) getIntent().getSerializableExtra("user")};

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);

        if (user[0] != null) {
            editTextTextPassword.setText(user[0].getPasswd());
            editTextTextPassword2.setText(user[0].getKeyPass());
            editTextTextPersonName.setText(user[0].getAddress());
            switch1.setChecked(user[0].isSwitch1());
            switch2.setChecked(user[0].isSwitch2());
            switch3.setChecked(user[0].isSwitch3());
        }

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(LoginActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service")
                .setPermissions(Manifest.permission.INTERNET)
                .check();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCredentialsUserInput()) {
                    return;
                }

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user", editTextTextPersonName.getText().toString())
                        .addFormDataPart("hash", editTextTextPassword.getText().toString())
                        .build();

                try {
                    Request request = null;
                    request = new Request.Builder()
                            .url(getFinalDestinationHost(editTextTextPersonName.getText().toString().split("//:")[0]) + "/inbox/mails")
                            .post(requestBody)
                            .build();


                    Response response = client.newCall(request).execute();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    PGPPlugKeyHandler keyHandler = new PGPPlugKeyHandler();

                    user[0] = new User();
                    user[0].setAddress(editTextTextPersonName.getText().toString());
                    user[0].setPasswd(editTextTextPassword.getText().toString());
                    user[0].setKeyPass(editTextTextPassword2.getText().toString());

                    user[0].setPublicKeyRing(keyHandler.fetchPublicKeyRingFromHost(user[0]));
                    user[0].setPrivateKeyRing(keyHandler.fetchPrivateKeyRingFromHost(user[0]));

                    SharedPreferences sharedPreferences = getSharedPreferences("SMailClient", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    if(switch1.isChecked()){
                        editor.putString("user", user[0].getAddress());
                    }
                    if(switch2.isChecked()){
                        editor.putString("pw", user[0].getPasswd());
                    }
                    if(switch3.isChecked()){
                        editor.putString("kp", user[0].getKeyPass());
                    }

                    editor.putString("sw1", boolToString(switch1.isChecked()));
                    editor.putString("sw2", boolToString(switch2.isChecked()));
                    editor.putString("sw3", boolToString(switch3.isChecked()));
                    editor.apply();


                    intent.putExtra("user", user[0]);
                    startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkCredentialsUserInput() {
        if (editTextTextPersonName.getText().length() == 0 ||
                editTextTextPassword.getText().toString().length() == 0 ||
                editTextTextPassword2.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Check credentials", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
