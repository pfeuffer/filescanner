package de.pfeufferweb.filewatch;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantToStringConverter implements Converter<Instant, String> {

    @Override
    public String convert(Instant instant) {
        return DateTimeFormatter.ISO_INSTANT.format(instant);
    }
}
