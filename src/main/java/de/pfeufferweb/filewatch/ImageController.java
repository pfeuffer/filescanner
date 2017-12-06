package de.pfeufferweb.filewatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/motioncontrol/image")
public class ImageController {

    private static Log LOG = LogFactory.getLog(ImageController.class);

    private final String directory;

    public ImageController(@Value("${directory}") String directory) {
        this.directory = directory;
    }

    @GetMapping("/{name:.+}")
    public ResponseEntity<InputStreamResource> readFile(@PathVariable("name") String name) {
        Path directoryPath = Paths.get(directory);

        Path filePath = directoryPath.resolve(name);
        long size;
        InputStream inputStream;
        try {
            size = Files.size(filePath);
            inputStream = Files.newInputStream(filePath);
        } catch (IOException e) {
            LOG.error("could not read file " + name, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok()
                .contentLength(size)
                .contentType(MediaType.valueOf("image/jpeg"))
                .header("Content-Disposition", "attachment; filename=" + name)
                .body(new InputStreamResource(inputStream));
    }
}
