package de.pfeufferweb.filewatch;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Component
public class ErrorCollector {
    private final List<ErrorEvent> errors = new ArrayList<>();

    public void addError(ErrorType type) {
        this.errors.add(new ErrorEvent(type));
    }

    public List<ErrorEvent> getErrors() {
        return unmodifiableList(errors);
    }

    public enum ErrorType {
        IMAGE_DIRECTORY_NOT_FOUND
    }

    public static class ErrorEvent {
        private final Instant time = Instant.now();
        private final ErrorType type;

        public ErrorEvent(ErrorType type) {
            this.type = type;
        }

        public Instant getTime() {
            return time;
        }

        public ErrorType getType() {
            return type;
        }
    }
}
