package info.gianlucacosta.historymark.gui.main.ages;

import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.util.HistoryDateFormat;
import info.gianlucacosta.zephyros.comparators.OptionalComparator;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;


class AgeItem implements Comparable<AgeItem> {
    public static final AgeItem ALL_TIME =
            new AgeItem();


    private static final Comparator<AgeItem> naturalComparator =
            Comparator.comparing(
                    AgeItem::getAge,
                    new OptionalComparator<>()
            );


    private final Optional<Age> ageOption;

    private AgeItem() {
        ageOption = Optional.empty();
    }

    public AgeItem(Age age) {
        ageOption = Optional.of(age);
    }


    @Override
    public int compareTo(AgeItem other) {
        return naturalComparator.compare(this, other);
    }

    public Optional<Age> getAge() {
        return ageOption;
    }


    public String getTitle() {
        return ageOption
                .map(age ->
                        String.format(
                                "%s (%s — %s)",
                                age.getName(),
                                HistoryDateFormat.formatDate(age.getStart()),
                                HistoryDateFormat.formatDate(age.getEnd())
                        ))
                .orElse("(ALL HISTORY)");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgeItem)) return false;
        AgeItem ageItem = (AgeItem) o;
        return Objects.equals(ageOption, ageItem.ageOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ageOption);
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
