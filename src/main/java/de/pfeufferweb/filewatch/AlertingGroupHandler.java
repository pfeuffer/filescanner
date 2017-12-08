package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Override
    public Collection<FileInfo> get(Instant group) {
        return latestGroups.get(group);
    }

    @Override
    public Collection<Instant> getGroups() {
        return latestGroups == null ? Collections.emptyList() : latestGroups.keySet();
    }

    private void handleDifferences(Map<Instant, List<FileInfo>> latest, Map<Instant, List<FileInfo>> current) {
        current.entrySet().stream()
                .filter(e -> !latest.containsKey(e.getKey()))
                .forEach(e -> alert(e.getKey(), e.getValue()));
    }

    private void alert(Instant instant, List<FileInfo> fileInfos) {
        LOG.info("alert for " + fileInfos.size() + " new file(s) at " + instant);
        alerter.alert(instant, fileInfos);
    }
}
