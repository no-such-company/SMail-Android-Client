package io.github.no_such_company.smailclientapp.helper;
/* 
    skalski created on 06/02/2021 inside the package - io.github.nosuchcompany.blackchamber.helper 
    Twitter: @KalskiSwen    
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static io.github.no_such_company.smailclientapp.helper.Sanitization.isDomain;

public class AlternateHostHelper {

    public static String getFinalDestinationHost(String probingHost) throws MalformedURLException {
        URL url = new URL(ProtocolHelper.getProtocol(probingHost) + probingHost + "/smail.redir");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
            for (String line; (line = reader.readLine()) != null;) {
                if(isDomain(line)){
                    return ProtocolHelper.getProtocol(line) + line;
                }
                return probingHost;
            }
        } catch (Exception e) {
            return probingHost;
        }
        return probingHost;
    }
}
