package org.kevoree.mwg.benchmark.json;

import org.kevoree.mwg.benchmark.AbstractBenchmark;

public class JsonFormat {

    public static String result2Json(AbstractBenchmark benchmark) {
        return "{\n" +
                "  \"bench\": \"" + benchmark.getClass().getSimpleName() + "\",\n" +
                "  \"configuration\": {\n" +
                "    \"roundsBefore\": " + benchmark.getRoundsBefore() + ",\n" +
                "    \"rounds\": " + benchmark.getRounds() + ",\n" +
                "    \"displayEach\": " + benchmark.getDisplayEach() + ",\n" +
                "    \"useOffHeap\": " + benchmark.isUseOffHeap() + ",\n" +
                "    \"cacheSize\": "+ benchmark.getCachesize() +"\n" +
                "  },\n" +
                "  \"benchmarkSpeed\": "+ benchmark.getBenchmarkspeed() +"\n" +
                "}";
    }

}
