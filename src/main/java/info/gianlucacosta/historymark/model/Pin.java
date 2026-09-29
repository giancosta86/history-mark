package info.gianlucacosta.historymark.model;

import info.gianlucacosta.historymark.util.HistoryDateFormat;
import org.jxmapviewer.viewer.GeoPosition;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.awt.Color;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"title", "latitude", "longitude", "date"}
                )
        }
)
public class Pin implements info.gianlucacosta.atlas.pin.CommonPin {
    @Id
    private UUID id;

    private String title;

    private Location location;

    private LocalDate date;

    private String encodedColor;

    @Column(length = 4096)
    private String description;


    private Pin() {
    }


    public Pin(
            String title,
            Location location,
            LocalDate date,
            String encodedColor,
            Optional<String> descriptionOption
    ) {
        this(
                UUID.randomUUID(),
                title,
                location,
                date,
                encodedColor,
                descriptionOption
        );
    }


    public Pin(
            UUID id,
            String title,
            Location location,
            LocalDate date,
            String encodedColor,
            Optional<String> descriptionOption
    ) {
        this.id = id;

        String actualTitle =
                title.trim();

        if (actualTitle.isEmpty()) {
            throw new IllegalArgumentException("The title is missing");
        }

        this.title = actualTitle;
        this.location = location;
        this.date = date;

        this.encodedColor = encodedColor;

        this.description =
                descriptionOption
                        .flatMap(description -> {
                            String trimmedDescription =
                                    description.trim();

                            return trimmedDescription.isEmpty() ?
                                    Optional.empty()
                                    :
                                    Optional.of(trimmedDescription);
                        })
                        .orElse(null);
    }


    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getEncodedColor() {
        return encodedColor;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public GeoPosition getPosition() {
        return location.toPosition();
    }

    @Override
    public Color getColor() {
        return Color.decode(encodedColor);
    }

    @Override
    public String getLabelText() {
        String descriptionString =
                getDescription()
                        .map(description ->
                                String.format(
                                        "<br /><br />%s",
                                        description.replaceAll("\n", "<br />")
                                )
                        )
                        .orElse("");

        return String.format(
                "<html><font size='6'><b>%s</b> (%s)</font><font size='4'><i>%s</i></font></html>",
                title,
                HistoryDateFormat.formatDate(date),
                descriptionString
        );
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pin)) return false;
        Pin pin = (Pin) o;
        return Objects.equals(id, pin.id) &&
                Objects.equals(title, pin.title) &&
                Objects.equals(location, pin.location) &&
                Objects.equals(date, pin.date) &&
                Objects.equals(encodedColor, pin.encodedColor) &&
                Objects.equals(description, pin.description);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, title, location, date, encodedColor, description);
    }


    @Override
    public String toString() {
        return "Pin{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", location=" + location +
                ", date=" + date +
                ", encodedColor='" + encodedColor + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}