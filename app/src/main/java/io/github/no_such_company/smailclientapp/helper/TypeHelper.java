package io.github.no_such_company.smailclientapp.helper;

public class TypeHelper {

    public static boolean stringToBool(String string){
        if(string.equals("1") || string.toLowerCase().equals("true") ){
            return true;
        }
        return false;
    }

    public static String boolToString(Boolean bool){
        if(bool){
            return "true";
        }

        return "false";
    }
}
