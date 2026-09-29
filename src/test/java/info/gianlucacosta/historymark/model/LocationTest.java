package info.gianlucacosta.historymark.model;

import org.junit.Test;
import org.jxmapviewer.viewer.GeoPosition;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class LocationTest {
    private final double LATITUDE = 34.8;
    private final double LONGITUDE = 156.7;


    private final Location referenceLocation =
            new Location(
                    LATITUDE,
                    LONGITUDE
            );


    private final GeoPosition referencePosition =
            new GeoPosition(
                    LATITUDE,
                    LONGITUDE
            );


    @Test
    public void gettersShouldWork() {
        assertThat(
                referenceLocation.getLatitude(),
                equalTo(LATITUDE)
        );

        assertThat(
                referenceLocation.getLongitude(),
                equalTo(LONGITUDE)
        );
    }


    @Test
    public void conversionFromPositionShouldWork() {
        Location convertedLocation =
                new Location(referencePosition);

        assertThat(
                convertedLocation,
                equalTo(referenceLocation)
        );
    }


    @Test
    public void conversionToPositionShouldWork() {
        GeoPosition convertedPosition =
                referenceLocation.toPosition();

        assertThat(
                convertedPosition,
                equalTo(referencePosition)
        );
    }
}
