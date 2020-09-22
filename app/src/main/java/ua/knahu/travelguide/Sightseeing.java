package ua.knahu.travelguide;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Локация.
 */
public class Sightseeing {

    public Sightseeing() {}

    /**
     * Конструктор.
     *
     * @param id
     * @param title
     * @param about
     * @param picture
     * @param coordinates
     */
    public Sightseeing(int id, String title, String about, Uri picture, LatLng coordinates) {
        this.id = id;
        this.title = title;
        this.about = about;
        this.picture = picture;
        this.coordinates = coordinates;
    }



    public int getId() {
        return id;
    }

    public LatLng getCoordinates() { return coordinates; }

    public void setCoordinates(LatLng coordinates) { this.coordinates = coordinates; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Uri getPicture() { return picture; }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    private Integer id;
    private String title;
    private String about;
    private Uri picture;
    private LatLng coordinates;
}
