package de.pfeufferweb.filewatch;

public class ProcessAlreadyRunningException extends ProcessStartupException {
    public ProcessAlreadyRunningException() {
        super("process already running");
    }
}
