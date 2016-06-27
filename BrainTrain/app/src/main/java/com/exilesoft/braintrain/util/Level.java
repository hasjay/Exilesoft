package com.exilesoft.braintrain.util;

/**
 * Created by hij on 6/16/2016.
 */
public enum Level {
    NOVICE(1),
    EASY(2),
    MEDIUM(3),
    GURU(4);

    private int val;

    Level(int val){
        this.val = val;
    }

    public int getLevel(){
        return val;
    }
}
