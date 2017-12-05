package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class AlertingGroupHandler implements GroupHandler {

    private static Log LOG = LogFactory.getLog(AlertingGroupHandler.class);

    private final Alerter alerter;

    private Map<Instant, List<FileInfo>> latestGroups = null;

    public AlertingGroupHandler(Alerter alerter) {
        this.alerter = alerter;
    }

    @Override
    public void handle(Map<Instant, List<FileInfo>> current) {
        if (latestGroups != null) {
            handleDifferences(latestGroups, current);
        }
        latestGroups = current;
    }

    private void handleDifferences(Map<Instant, List<FileInfo>> latest, Map<Instant, List<FileInfo>> current) {
        current.entrySet().stream()
                .filter(e -> !latest.containsKey(e.getKey()))
                .map(Entry::getValue)
                .forEach(this::alert);
    }

    private void alert(List<FileInfo> fileInfos) {
        LOG.info("alert for " + fileInfos.size() + " new file(s)");
        alerter.alert(fileInfos);
    }
}
