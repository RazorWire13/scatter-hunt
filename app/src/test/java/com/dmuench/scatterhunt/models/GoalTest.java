package com.dmuench.scatterhunt.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class GoalTest {

    @Test
    public void goalConstructor() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals("test title", goal.getTitle());
        assertEquals("test user", goal.getCreatedBy());
        assertEquals("test array clue one", goal.getClueOne());
    }

    @Test
    public void getTitle() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals("test title", goal.getTitle());
    }

    @Test
    public void getLatitude() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals(1.0, goal.getLatitude(), 0);
    }

    @Test
    public void getLongitude() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals(2.0, goal.getLongitude(), 0);
    }

    @Test
    public void getGeoDistance() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals(3, goal.getGeoDistance(), 0);
    }

    @Test
    public void getClueOne() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals("test array clue one", goal.getClueOne());
    }

    @Test
    public void getClueTwo() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals("two", goal.getClueTwo());
    }

    @Test
    public void getClueThree() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals("three", goal.getClueThree());
    }

    @Test
    public void getCreatedBy() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        assertEquals("test user", goal.getCreatedBy());
    }

    @Test
    public void setTitle() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setTitle("test changed");
        assertEquals("test changed", goal.getTitle());
    }

    @Test
    public void setLongitude() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setLongitude(1.0);
        assertEquals(1.0, goal.getLongitude(), 0);
    }

    @Test
    public void setLatitude() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setLatitude(3.0);
        assertEquals(3.0, goal.getLatitude(), 0);
    }

    @Test
    public void setGeoDistance() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setGeoDistance(1);
        assertEquals(1, goal.getGeoDistance(), 0);
    }

    @Test
    public void setClueOne() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setClueOne("test changed");
        assertEquals("test changed", goal.getClueOne());
    }

    @Test
    public void setClueTwo() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setClueTwo("test changed");
        assertEquals("test changed", goal.getClueTwo());
    }

    @Test
    public void setClueThree() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setClueThree("test changed");
        assertEquals("test changed", goal.getClueThree());
    }

    @Test
    public void setCreatedBy() {
        Goal goal = new Goal("test title", 1.0, 2.0, 3, new String[]{"test array clue one", "two", "three"}, "test user");

        goal.setCreatedBy("test changed");
        assertEquals("test changed", goal.getCreatedBy());
    }
}