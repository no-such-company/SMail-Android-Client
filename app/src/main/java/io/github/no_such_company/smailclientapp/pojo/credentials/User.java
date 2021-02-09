package io.github.no_such_company.smailclientapp.pojo.credentials;

import java.io.Serializable;

public class User implements Serializable {

    private String address;
    private String passwd;
    private String keyPass;
    private boolean switch1;
    private boolean switch2;
    private boolean switch3;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public void storeCredentials() {

    }

    public void regenerateCredentials() {
    }

    public boolean isSwitch1() {
        return switch1;
    }

    public void setSwitch1(boolean switch1) {
        this.switch1 = switch1;
    }

    public boolean isSwitch2() {
        return switch2;
    }

    public void setSwitch2(boolean switch2) {
        this.switch2 = switch2;
    }

    public boolean isSwitch3() {
        return switch3;
    }

    public void setSwitch3(boolean switch3) {
        this.switch3 = switch3;
    }
}
