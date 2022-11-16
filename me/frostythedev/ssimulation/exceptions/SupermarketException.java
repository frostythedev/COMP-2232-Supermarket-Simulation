package me.frostythedev.ssimulation.exceptions;

import me.frostythedev.ssimulation.LogRecord;

import java.util.concurrent.RecursiveTask;

public class SupermarketException extends Exception {

    private final long dateTime;
    private final String message;

    public SupermarketException(String message){
        this.dateTime = System.currentTimeMillis();
        this.message = message;
    }

    public LogRecord getRecord(){
        return new LogRecord(dateTime, message);
    }
}
