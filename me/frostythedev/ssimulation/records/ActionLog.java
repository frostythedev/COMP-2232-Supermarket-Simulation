package me.frostythedev.ssimulation.records;

public class ActionLog {

    private long cycleIndex;
    private String logMessage;

    public ActionLog(long cycleIndex, String logMessage) {
        this.cycleIndex = cycleIndex;
        this.logMessage = logMessage;
    }

    // GETTERS and MUTATORS for cycleIndex and logMessage of actionLog
    public long getCycleIndex() {
        return cycleIndex;
    }

    public void setDateTime(long dateTime) {
        this.cycleIndex = dateTime;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
