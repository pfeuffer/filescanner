package de.pfeufferweb.filewatch;

import org.springframework.core.convert.converter.Converter;

import java.time.Instant;

public class StringToInstantConverter implements Converter<String, Instant> {

    @Override
    public Instant convert(String instantString) {
        return Instant.parse(instantString);
    }
}
