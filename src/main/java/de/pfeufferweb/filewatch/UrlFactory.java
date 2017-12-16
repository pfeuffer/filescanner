package de.pfeufferweb.filewatch;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;

// Due to the fact, that hateoas support in spring in the used version
// only seems to work from web requests, we have to build our urls on our own.
@Component
public class UrlFactory {

    private final Environment environment;

    public UrlFactory(Environment environment) {
        this.environment = environment;
    }

    public String createImageUrl(String fileName) {
        return baseUrl() + "/image/" + fileName;
    }

    public String createGroupUrl(Instant instant) {
        return baseUrl() + "/view/" + new InstantToStringConverter().convert(instant);
    }

    public String hostPart() {
        return environment.getProperty("server.protocol") +
                "://" +
                environment.getProperty("server.hostname") +
                (environment.getProperty("server.port") == null
                        ? ""
                        : ":" + environment.getProperty("server.port"));
    }

    private String baseUrl() {
        return "/motioncontrol";
    }
}
