package io.github.no_such_company.smailclientapp.pojo.credentials;

import java.io.Serializable;

public class User implements Serializable {

    private String address;
    private String passwd;
    private String keyPass;

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

    public void storeCredentials(){

    }

    public void regenerateCredentials(){
    }
}
