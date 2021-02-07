package io.github.no_such_company.smailclientapp.helper;

import static io.github.no_such_company.smailclientapp.helper.Sanitization.isIp4Address;

public class ProtocolHelper {

    public static final String LOCALHOST = "localhost";
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String LOCALHOST_IP = "127.0.0.1";

    public static String getProtocol(String host) {
        if (host.equals(LOCALHOST) || isIp4Address(host) && host.equals(LOCALHOST_IP)) {
            return HTTP;
        }

        return HTTPS;
    }
}
