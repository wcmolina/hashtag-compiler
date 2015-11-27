package com.compiler.codegeneration;

/**
 * Created by Wilmer Carranza on 26/06/2015.
 */
public class Temporal {

    private boolean free;
    private String name;

    public Temporal(String name, boolean free) {
        this.free = free;
        this.name = name;
    }

    public Temporal(String name) {
        this.name = name;
        free = true;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getName() {
        return name;
    }
}
