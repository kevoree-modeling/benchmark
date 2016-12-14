package org.mwg.benchmark.storage;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

public abstract class AbstractBenchTest {
    protected String _url = "http://0.0.0.0:9876";
    protected String _bench;
    protected int _roundsBefore;
    protected int _rounds;
    protected int _displayEach;
    protected Boolean _useOffHeap;
    protected int _cachesize;

    protected abstract void init();

    @Test
    public void bench() {
        init();
        Assert.assertNotNull(_url);
        Assert.assertNotNull(_bench);
        Assert.assertNotNull(_useOffHeap);
        Assert.assertNotEquals(-1,_rounds);
        Assert.assertNotEquals(-1,_roundsBefore);
        Assert.assertNotEquals(-1,_displayEach);
        Assert.assertNotEquals(-1,_cachesize);

        URL[] urls = ((URLClassLoader) (Thread.currentThread().getContextClassLoader())).getURLs();
        StringBuilder cp = new StringBuilder();
        for(int i=0;i<urls.length;i++) {
            cp.append(urls[i].toString());
            if(i < urls.length - 2) {
                cp.append(":");
            }
        }

        ProcessBuilder process = new ProcessBuilder("java","-classpath", cp.toString(),"org.kevoree.mwg.benchmark.ExecuteBenchmark",
                _bench,_roundsBefore + "", _rounds + "", _displayEach + "", _useOffHeap + "",_cachesize + "",_url);
        process.redirectErrorStream(true);
        try {
            Process p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println(p.waitFor());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
