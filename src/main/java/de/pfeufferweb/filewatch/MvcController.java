package de.pfeufferweb.filewatch;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MvcController {

    private final ProcessRunner processRunner;

    public MvcController(ProcessRunner processRunner) {
        this.processRunner = processRunner;
    }

    @GetMapping(value = "", produces = {"text/html"})
    public String index(Model model) {
        model.addAttribute("running", processRunner.isRunning());
        return "index";
    }

    @PostMapping(value = "/start", produces = {"text/html"})
    public String start(Model model) {
        try {
            processRunner.start();
        } catch (ProcessStartupException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("running", processRunner.isRunning());
        return "index";
    }

    @PostMapping(value = "/stop", produces = {"text/html"})
    public String stop(Model model) {
        processRunner.stop();
        model.addAttribute("running", processRunner.isRunning());
        return "index";
    }
}
