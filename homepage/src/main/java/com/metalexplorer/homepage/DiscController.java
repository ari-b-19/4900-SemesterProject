package com.metalexplorer.homepage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.loki.afro.metallum.enums.DiscType;
import com.github.loki.afro.metallum.search.API;
import com.github.loki.afro.metallum.search.query.entity.DiscQuery;
import com.github.loki.afro.metallum.search.query.entity.SearchDiscResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DiscController {

    private static final String JSON_FILE_PATH = "albums.json";
    private AlbumInfo album;

    private String genre;

    @RequestMapping
    public void getReleases() {
        if (shouldUpdateJsonFile()) {
            List<AlbumInfo> albums = retrieveAlbums();

            writeToJsonFile(albums);
        } else {
            System.out.println("JSON file is up to date. No need to update.");
        }
    }

    private boolean shouldUpdateJsonFile() {
        File jsonFile = new File(JSON_FILE_PATH);
        if (!jsonFile.exists()) {
            return true; // File doesn't exist, update is needed
        }

        try {
            BasicFileAttributes attr = Files.readAttributes(jsonFile.toPath(), BasicFileAttributes.class);
            LocalDate lastModifiedDate = attr.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            return !isSameMonth(lastModifiedDate, currentDate);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isSameMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonth() == date2.getMonth();
    }

    private List<AlbumInfo> retrieveAlbums() {
        List<AlbumInfo> albums = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        DiscQuery query = DiscQuery.builder()
                .discType(DiscType.FULL_LENGTH)
                .fromYear(currentYear)
                .fromMonth(currentMonth)
                .toYear(currentYear)
                .toMonth(currentMonth)
                .build();

        for (SearchDiscResult discResult : API.getDiscs(query)) {
            discResult.getReleaseDate().ifPresent(optionalDateString -> {
                if (!optionalDateString.contains("-00")) {
//                    if (discResult.getGenre().isPresent()) {
//                        genre = discResult.getGenre().get();
//                    } else {
//                        genre = "";
//                    }
                    discResult.getGenre();
                    album = new AlbumInfo(discResult.getBandName(), discResult.getId(), discResult.getGenre().toString(), optionalDateString);
                    albums.add(album);
                }
            });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return albums;
    }

    private void writeToJsonFile(List<AlbumInfo> albums) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), albums);
            System.out.println("JSON data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
