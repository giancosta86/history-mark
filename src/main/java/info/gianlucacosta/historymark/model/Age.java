package info.gianlucacosta.historymark.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Age implements Comparable<Age> {
    private static Comparator<Age> naturalComparator =
            Comparator
                    .comparing(Age::getName)
                    .thenComparing(Age::getStart)
                    .thenComparing(Age::getEnd);

    @Id
    private UUID id;

    @Column(unique = true)
    private String name;

    private LocalDate start;
    private LocalDate end;


    private Age() {
    }


    public Age(String name, LocalDate start, LocalDate end) {
        this(
                UUID.randomUUID(),
                name,
                start,
                end
        );
    }


    public Age(UUID id, String name, LocalDate start, LocalDate end) {
        this.id = id;

        String actualName =
                name.trim();

        if (actualName.isEmpty()) {
            throw new IllegalArgumentException("The name is missing");
        }

        this.name = actualName;


        if (start.isAfter(end)) {
            throw new IllegalArgumentException("The start date cannot occur after the end date");
        }

        this.start = start;
        this.end = end;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }


    public boolean includes(LocalDate date) {
        return !date.isBefore(start) &&
                !date.isAfter(end);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Age)) return false;
        Age age = (Age) o;
        return Objects.equals(id, age.id) &&
                Objects.equals(name, age.name) &&
                Objects.equals(start, age.start) &&
                Objects.equals(end, age.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, start, end);
    }


    @Override
    public int compareTo(Age o) {
        return naturalComparator.compare(this, o);
    }

    @Override
    public String toString() {
        return "Age{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
