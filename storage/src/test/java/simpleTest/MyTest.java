package simpleTest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.kevoree.mwg.benchmark.rest.RestAPI;

public class MyTest {

    @Test
    public void test() {
        RestAPI restAPI = new RestAPI();
        restAPI.start(12345);

        try {
            HttpResponse<String> response = Unirest.get("http://localhost:12345")
                    .header("accept","application/json")
                    .asString();

//            System.out.println(response.getBody());
//            System.out.println(response.getStatus());



            HttpResponse<String> response2 = Unirest.post("http://localhost:12345")
                    .header("accept","application/json")
                    .body("{\n" +
                            "  \"bench\": \"TODO\",\n" +
                            "  \"configuration\": {\n" +
                            "    \"roundsBefore\": 1,\n" +
                            "    \"rounds\": 2,\n" +
                            "    \"displayEach\": 3,\n" +
                            "    \"useOffHeap\": true,\n" +
                            "    \"cacheSize\": 4\n" +
                            "  },\n" +
                            "  \"benchmarkSpped\": 55\n" +
                            "}")
                    .asString();

//            System.out.println(response2.getBody());
//            System.out.println(response2.getStatus());

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        restAPI.stop();
    }
}
