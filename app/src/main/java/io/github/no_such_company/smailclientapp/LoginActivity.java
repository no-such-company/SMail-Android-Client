package io.github.no_such_company.smailclientapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.List;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;
import io.github.no_such_company.smailclientapp.pojo.mailList.MailBox;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextTextPersonName;
    EditText editTextTextPassword;
    EditText editTextTextPassword2;
    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        client = new OkHttpClient();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        User user = (User) getIntent().getSerializableExtra("user");

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);

        if(user != null) {
            editTextTextPassword.setText(user.getPasswd());
            editTextTextPersonName.setText(user.getAddress());
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

                Request request = new Request.Builder()
                        .url("https://" + editTextTextPersonName.getText().toString().split("//:")[0] + "/inbox/mails")
                        .post(requestBody)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    user.setAddress(editTextTextPersonName.getText().toString());
                    user.setPasswd(editTextTextPassword.getText().toString());
                    user.setKeyPass(editTextTextPassword2.getText().toString());

                    intent.putExtra("user", user);
                    startActivity(intent);

                } catch (IOException e) {
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
