package de.pfeufferweb.filewatch;

import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;

// Due to the fact, that hateoas support in spring in the used version
// only seems to work from web requests, we have to build our urls on our own.
@Component
public class UrlFactory {

    private final Environment environment;
    private final EmbeddedWebApplicationContext context;

    public UrlFactory(Environment environment, EmbeddedWebApplicationContext context) {
        this.environment = environment;
        this.context = context;
    }

    public String createImageUrl(String fileName) {
        return baseUrl() + "/image/" + fileName;
    }

    public String createGroupUrl(Instant instant) {
        return baseUrl() + "/view/" + new InstantToStringConverter().convert(instant);
    }

    private String baseUrl() {
        return environment.getProperty("server.protocol") +
                "://" +
                environment.getProperty("server.hostname") +
                ":" +
                context.getEmbeddedServletContainer().getPort() +
                "/motioncontrol";
    }
}
