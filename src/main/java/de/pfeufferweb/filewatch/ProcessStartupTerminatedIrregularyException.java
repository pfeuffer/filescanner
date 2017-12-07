package de.pfeufferweb.filewatch;

public class ProcessStartupTerminatedIrregularyException extends ProcessStartupException {
    public ProcessStartupTerminatedIrregularyException() {
        super("process not started as expected");
    }
}
