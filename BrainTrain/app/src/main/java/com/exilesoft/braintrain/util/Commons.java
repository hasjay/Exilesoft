package com.exilesoft.braintrain.util;

/**
 * Created by hij on 6/22/2016.
 */
public final class Commons {
    public static final String GAME_STATE = "brain.train.state";
    public final static int NO_OF_LAP = 3;
    public final static int NO_OF_ATTEMPTS = 4;

    public static boolean validate(final String text){
        return ! text.contains("?") || text.contains(".") || "".equals(text);
    }
}
