package com.metalexplorer;

public class RecentReleasesData {

    private String bandName;
    private long id;
    private String genre;
    private String releaseDate;

    public RecentReleasesData() {
    }

    public RecentReleasesData(String bandName, long id, String genre, String releaseDate) {
        this.bandName = bandName;
        this.id = id;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public String getBandName() {
        return bandName;
    }

    public long getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
