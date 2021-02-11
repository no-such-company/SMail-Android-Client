package io.github.no_such_company.smailclientapp.helper;

public class AddressHelper {

    public static String getHostFromAddress(String username){
        return username.split("//:")[0];
    }
}
