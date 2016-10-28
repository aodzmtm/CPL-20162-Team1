package com.example.safelight;

/**
 * Created by adnim on 2015-06-25.
 */
public class Node {
    private String name;
    private int level;
    private float x;
    private float y;

    public Node(String name, int level, float x, float y) {
        this.name = name;
        this.level = level;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
