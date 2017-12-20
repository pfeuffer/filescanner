package de.pfeufferweb.filewatch;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MvcController {

    private final ProcessRunner processRunner;
    private final GroupHandler groupHandler;
    private final UrlFactory urlFactory;

    public MvcController(ProcessRunner processRunner, GroupHandler groupHandler, UrlFactory urlFactory) {
        this.processRunner = processRunner;
        this.groupHandler = groupHandler;
        this.urlFactory = urlFactory;
    }

    @GetMapping(value = "", produces = {"text/html"})
    public String index(Model model) {
        indexModel(model);
        return "index";
    }

    @PostMapping(value = "/start", produces = {"text/html"})
    public String start() {
        try {
            processRunner.start();
        } catch (ProcessStartupException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    @PostMapping(value = "/stop", produces = {"text/html"})
    public String stop() {
        processRunner.stop();
        return "redirect:/";
    }

    @GetMapping(value = "/view/{group}", produces = {"text/html"})
    public String group(@PathVariable("group") Instant group, Model model) {
        Collection<FileInfo> fileInfos = groupHandler.get(group);
        model.addAttribute("fileUrls", toImageUrls(fileInfos));
        return "group";
    }

    private void indexModel(Model model) {
        model.addAttribute("running", processRunner.isRunning());
        model.addAttribute("groupUrls", toGroupUrls(groupHandler.getGroups()));
    }

    private List<String> toImageUrls(Collection<FileInfo> fileInfos) {
        return fileInfos
                .stream()
                .map(FileInfo::getName)
                .sorted()
                .map(urlFactory::createImageUrl)
                .collect(Collectors.toList());
    }

    private List<GroupWithUrl> toGroupUrls(Collection<Instant> groups) {
        return groups
                .stream()
                .map(g -> new GroupWithUrl(g, urlFactory.createGroupUrl(g)))
                .sorted((g1, g2) -> g2.group.compareTo(g1.group))
                .limit(5)
                .collect(Collectors.toList());
    }

    public class GroupWithUrl {
        private final Instant group;
        private final String url;

        public GroupWithUrl(Instant group, String url) {
            this.group = group;
            this.url = url;
        }

        public Instant getGroup() {
            return group;
        }

        public String getUrl() {
            return url;
        }
    }
}
