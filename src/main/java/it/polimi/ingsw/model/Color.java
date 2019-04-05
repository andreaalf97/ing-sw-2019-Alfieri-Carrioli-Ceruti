package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Color {
    RED,
    YELLOW,
    BLUE;

    private static final List<Color> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Color randomColor(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    //https://stackoverflow.com/questions/1972392/pick-a-random-value-from-an-enum

}
