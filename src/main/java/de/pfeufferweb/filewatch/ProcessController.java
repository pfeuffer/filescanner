package de.pfeufferweb.filewatch;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProcessController {

    private final ProcessRunner processRunner;

    public ProcessController(ProcessRunner processRunner) {
        this.processRunner = processRunner;
    }

    @PostMapping("/start")
    public ResponseEntity<String> start() {
        if (processRunner.isRunning()) {
            return ResponseEntity.badRequest().body("process already running");
        }
        boolean started = processRunner.start();
        if (started) {
            return ResponseEntity.ok("process started successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("process ended");
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stop() {
        if (!processRunner.isRunning()) {
            return ResponseEntity.badRequest().body("process already stopped");
        }
        processRunner.stop();
        return ResponseEntity.ok("process stopped");
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        if (processRunner.isRunning()) {
            return ResponseEntity.ok("process running");
        } else {
            return ResponseEntity.ok("process terminated");
        }
    }
}
