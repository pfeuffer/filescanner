package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

@Component
public class ProcessRunner {

    private static Log LOG = LogFactory.getLog(ProcessRunner.class);

    private final String startupCommand;
    private final Pattern startupCheck;

    private Process process;

    public ProcessRunner(
            @Value("${startup.command}") String startupCommand,
            @Value("${startup.check.pattern}") String startupCheckPattern) {
        this.startupCommand = startupCommand;
        this.startupCheck = Pattern.compile(startupCheckPattern);
    }

    public boolean isRunning() {
        return process != null && process.isAlive();
    }

    public void start() throws ProcessStartupException {
        if (isRunning()) {
            throw new ProcessAlreadyRunningException();
        }
        LOG.info("starting motion process");
        try {
            process = Runtime.getRuntime().exec(startupCommand, null, null);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = in.readLine()) != null) {
                LOG.info("process out: " + line);
                if (startupCheck.matcher(line).matches()) {
                    LOG.info("motion started successfully");
                    return;
                }
            }
        } catch (Exception e) {
            throw new ProcessStartupException(e);
        }
        LOG.info("could not start motion process");
        throw new ProcessStartupTerminatedIrregularyException();
    }

    public void stop() {
        LOG.info("stopping motion process");
        process.destroy();
        try {
            process.waitFor();
            LOG.info("stopped motion process");
        } catch (InterruptedException e) {
            LOG.error("error stopping process", e);
        }
    }
}
