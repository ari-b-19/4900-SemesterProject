package com.metalexplorer.homepage;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class JSONController {

    private static final String JSON_FILE_PATH = "albums.json";

    @GetMapping(value = "/albums", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAlbums() throws IOException {
        return new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
    }
}