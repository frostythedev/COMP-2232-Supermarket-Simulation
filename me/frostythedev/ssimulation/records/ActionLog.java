package me.frostythedev.ssimulation.records;

/*
 * PROGRAMMERS: Tevin Cole, Jalisa Thompson, Simone Doughlin-Welsh
 *
 * This class defines the characteristics of the ActionLog which contains an identifying cycleIndex, and corresponding
 *  logMessage which is stored. It also defines useful getters and mutators for the fields of the class for easy
 * accessibility.
 * */
public class ActionLog {

    // Stores the index in which the action was logged
    private int cycleIndex;
    private String logMessage;

    public ActionLog(int cycleIndex, String logMessage) {
        this.cycleIndex = cycleIndex;
        this.logMessage = logMessage;
    }

    // GETTERS and MUTATORS for cycleIndex and logMessage of actionLog
    public int getCycleIndex() {
        return cycleIndex;
    }

    public void setCycleIndex(int index) {
        this.cycleIndex = index;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
