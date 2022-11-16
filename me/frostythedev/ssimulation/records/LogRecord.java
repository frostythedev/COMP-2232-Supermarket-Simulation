package me.frostythedev.ssimulation.records;

public class LogRecord {

    private long cycleIndex;
    private String logMessage;

    public LogRecord(long cycleIndex, String logMessage) {
        this.cycleIndex = cycleIndex;
        this.logMessage = logMessage;
    }


    // GETTERS and MUTATORS for cycleIndex and logMessage of record
    public long getCycleIndex() {
        return cycleIndex;
    }

    public void setCycleIndex(long cycleIndex) {
        this.cycleIndex = cycleIndex;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
