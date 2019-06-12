package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Color {
    RED("\u001b[31m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    ANY("\u001B[0m"),




    GREEN("\u001b[32m"),
    WHITE("\u001b[37m"),
    BLACK("\u001b[30m"),
    PURPLE("\u001b[35m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    Color(String escape){
        this.escape = escape;
    }

    public String escape(){
        return escape;
    }

    /**
     * The list of all possible values
     */
    private static final List<Color> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

    /**
     * The size of the values array
     */
    private static final int SIZE = VALUES.size();

    /**
     * A Random object used to pick a random color
     */
    private static final Random RANDOM = new Random();

    /**
     * Picks a random color out of the 3 values RED/BLUR/YELLOW
     * @return a random Color object
     */
    public static Color randomColor(){
        return VALUES.get(RANDOM.nextInt(3));
    }

    //https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum

}
