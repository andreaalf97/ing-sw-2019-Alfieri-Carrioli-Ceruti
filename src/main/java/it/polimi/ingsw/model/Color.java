package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Color {
    RED,
    YELLOW,
    BLUE;

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
     * Picks a random color out of the 3 values
     * @return a random Color object
     */
    public static Color randomColor(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    //https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum

}
