package org.kevoree.mwg.benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ludovicmouline on 05/12/2016.
 */
public class PomVersion {
    private static final String pomUrl = "https://raw.githubusercontent.com/kevoree-modeling/mwDB/master/pom.xml";

    public static void getPomVersion() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(pomUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(60_000);

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            int idxStartIndex = -1;
            String version = null;
            while (version == null && (line = rd.readLine()) != null) {
                idxStartIndex = -1;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '<') {
                        if(idxStartIndex != -1 && i + 9 < line.length() && line.charAt(i+1) == '/') {
                            version = line.substring(idxStartIndex,i);
                            break;
                        }
                        if(i + 8 < line.length() && line.charAt(i+8) == '>') {
                            if(line.charAt(i+1) == 'v' && line.charAt(i+2) == 'e'  && line.charAt(i+3) == 'r' && line.charAt(i+4) == 's'
                                    && line.charAt(i+5) == 'i' && line.charAt(i+6) == 'o' && line.charAt(i+7) == 'n') {
                                idxStartIndex = i + 9;
                            }
                        }
                    }
                }
            }
            rd.close();
            System.out.println(version);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PomVersion.getPomVersion();
    }
}
