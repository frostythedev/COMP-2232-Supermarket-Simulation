package me.frostythedev.ssimulation.exceptions;

import me.frostythedev.ssimulation.records.LogRecord;

public class SupermarketException extends Exception {

    private final long dateTime;
    private final String message;

    public SupermarketException(String message){
        super(message);
        this.dateTime = System.currentTimeMillis();
        this.message = message;
    }

    public LogRecord getRecord(){
        return new LogRecord(dateTime, message);
    }
}
