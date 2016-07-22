package org.kevoree.mwg.benchmark.connector;

/**
 * Created by ludovicmouline on 22/07/16.
 */
public class Configuration {
    private int roundsBefore;
    private int rounds;
    private int displayEach;
    private boolean useOffHeap;
    private int cacheSize;

    public int getRoundsBefore() {
        return roundsBefore;
    }

    public void setRoundsBefore(int roundsBefore) {
        this.roundsBefore = roundsBefore;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public int getDisplayEach() {
        return displayEach;
    }

    public void setDisplayEach(int displayEach) {
        this.displayEach = displayEach;
    }

    public boolean isUseOffHeap() {
        return useOffHeap;
    }

    public void setUseOffHeap(boolean useOffHeap) {
        this.useOffHeap = useOffHeap;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Override
    public String toString() {
        return roundsBefore + "," + rounds + "," + displayEach + "," + useOffHeap + "," + cacheSize;
    }
}
