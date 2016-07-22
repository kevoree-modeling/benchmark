package org.kevoree.mwg.benchmark.rest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by ludovicmouline on 22/07/16.
 */
public class Storage {

    public static void save(final String url, final String data) {
        System.err.println(data);
        try {
            HttpResponse<String> response2 = Unirest.post(url)
                    .header("accept","application/json")
                    .body(data)
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            //log error
        }
    }
}
