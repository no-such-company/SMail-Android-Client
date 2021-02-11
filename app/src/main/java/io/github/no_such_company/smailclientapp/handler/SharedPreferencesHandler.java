package io.github.no_such_company.smailclientapp.handler;

import android.content.Context;
import android.content.SharedPreferences;

import io.github.no_such_company.smailclientapp.pojo.credentials.User;

import static android.content.Context.MODE_PRIVATE;
import static io.github.no_such_company.smailclientapp.helper.TypeHelper.stringToBool;

public class SharedPreferencesHandler {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHandler(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SMail_client", MODE_PRIVATE);
    }

    public User fetchUserObjectFromStorage() {
        User user = new User();
        user.setAddress(sharedPreferences.getString("user", null));
        user.setPasswd(sharedPreferences.getString("pw", null));
        user.setKeyPass(sharedPreferences.getString("kp", null));
        user.setSwitch1(stringToBool(sharedPreferences.getString("sw1", "false")));
        user.setSwitch2(stringToBool(sharedPreferences.getString("sw2", "false")));
        user.setSwitch3(stringToBool(sharedPreferences.getString("sw3", "false")));

        return user;
    }
}
