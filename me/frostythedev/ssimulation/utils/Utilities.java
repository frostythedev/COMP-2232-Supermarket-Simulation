package me.frostythedev.ssimulation.utils;

import java.util.Random;

public class Utilities {

    private static Random random;

    public static void init(){
        random = new Random();
    }

    public static int generateRndNumber(int upper){
        return generateRndNumber(0, upper);
    }

    public static int generateRndNumber(int origin, int upper){
        return random.nextInt(origin, upper);
    }
}
