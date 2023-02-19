package com.peterfab.serversidesorting;

public interface Sortable {
    Iterable<Integer> getSortSlots();
    Iterable<Integer> getRefillSlots();
}
