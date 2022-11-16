package me.frostythedev.ssimulation.records;

public class ActionLog {

    private long dateTime;
    private String logMessage;

    public ActionLog(long dateTime, String logMessage) {
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
