package me.frostythedev.ssimulation.exceptions;

import me.frostythedev.ssimulation.records.LogRecord;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simeone Douglin-Welch
 *
 * This class defines the characteristics of a SupermarketException which all contain a corresponding message
 * detailing the error of the exception
 * */
public class SupermarketException extends Exception {


    // Stores the cycle number in which the

    // Class constructor which takes in a string detailing the error of the exception
    public SupermarketException(String message){
        super(message);
    }

    // Returns a logRecord of the exception
    public LogRecord getRecord(){
        return new LogRecord(-1, getMessage());
    }
}
