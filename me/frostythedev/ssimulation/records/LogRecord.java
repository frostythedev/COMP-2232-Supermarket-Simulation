package me.frostythedev.ssimulation.records;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simeone Douglin-Welch
 *
 * This class defines the characteristics of the LogRecord which contains an identifying cycleIndex, and corresponding
 *  logMessage which is stored. It also defines useful getters and mutators for the fields of the class for easy
 * accessibility.
 * */
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
