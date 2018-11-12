package dk.aau.cs.ds302e18.service;

import dk.aau.cs.ds302e18.service.models.Lesson;

import java.util.Comparator;

public class SortByID implements Comparator<Lesson> {
    @Override
    public int compare(Lesson o1, Lesson o2) {
        return (int)o2.getId()-(int)o1.getId();
    }
}