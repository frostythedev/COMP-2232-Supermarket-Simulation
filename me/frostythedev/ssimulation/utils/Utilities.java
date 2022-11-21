package me.frostythedev.ssimulation.utils;

import java.util.Random;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
 *
 * This class defines a globally accessible random function which was used through the program for number generation
 * */
public class Utilities {

    private static Random random;

    public static void init(){
        random = new Random();
    }

    // Generates a random number between 0 (inclusive) and upper (exclusive)
    public static int generateRndNumber(int upper){
        return generateRndNumber(0, upper);
    }

    // Generates a random number between origin (inclusive) and upper (exclusive)
    // From JAVA 17 Random implements from RandomGeneration which defines a default implementation of random.nextInt
    // (int, int) https://openjdk.org/jeps/356 compiling with the newest version of JAVA which is specificed in this
    // course's outline will not product a compilation error as seen from the used import java.util.Random.
    public static int generateRndNumber(int origin, int upper){
        return random.nextInt(origin, upper);
    }
}
