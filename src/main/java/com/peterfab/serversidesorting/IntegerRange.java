package com.peterfab.serversidesorting;

import java.util.Iterator;
import java.util.stream.IntStream;

public class IntegerRange implements Iterable<Integer> {
    int start;
    int stop;

    public IntegerRange(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    @Override
    public Iterator<Integer> iterator() {
        return IntStream.range(start, stop).iterator();
    }
}
