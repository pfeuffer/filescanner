package de.pfeufferweb.filewatch;

public class ProcessStartupException extends Exception {
    public ProcessStartupException(String message) {
        super(message);
    }

    public ProcessStartupException(Throwable cause) {
        super(cause);
    }
}
