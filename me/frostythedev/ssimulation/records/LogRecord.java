package me.frostythedev.ssimulation.records;

public class LogRecord {

    private long dateTime;
    private String logMessage;

    public LogRecord(long dateTime, String logMessage) {
        this.dateTime = dateTime;
        this.logMessage = logMessage;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
