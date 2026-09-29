package info.gianlucacosta.historymark.model;

import org.jxmapviewer.viewer.GeoPosition;

import javax.persistence.Embeddable;
import java.util.Objects;

/**
 * GPS location, expressed in latitude and longitude
 */
@Embeddable
public class Location {
    private double latitude;
    private double longitude;

    private Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Location(GeoPosition position) {
        this(
                position.getLatitude(),
                position.getLongitude()
        );
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    public GeoPosition toPosition() {
        return new GeoPosition(
                latitude,
                longitude
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location that = (Location) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
