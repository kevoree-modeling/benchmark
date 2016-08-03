package org.kevoree.mwg.benchmark.utils;


import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class RestCall {

    public static void save(final String url, final String data) {
        System.err.println(data);
        try {
            Unirest.post(url)
                    .header("accept","application/json")
                    .body(data)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

}
