package info.gianlucacosta.historymark.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class AgeTest {
    private final Age referenceAge =
            TestObjects.alphaAge;


    @Test(expected = IllegalArgumentException.class)
    public void emptyNamesShouldNotBeAccepted() {
        new Age(
                "",
                referenceAge.getStart(),
                referenceAge.getEnd()
        );
    }


    @Test(expected = IllegalArgumentException.class)
    public void namesHavingOnlySpacesShouldNotBeAccepted() {
        new Age(
                "  \t      \t  ",
                referenceAge.getStart(),
                referenceAge.getEnd()
        );
    }


    @Test
    public void namesShouldBeTrimmed() {
        Age testAge =
                new Age(
                        "   Example  ",
                        referenceAge.getStart(),
                        referenceAge.getEnd()
                );

        assertThat(
                testAge.getName(),
                equalTo("Example")
        );
    }


    @Test(expected = IllegalArgumentException.class)
    public void startDatesAfterEndDatesShouldNotBeAccepted() {
        new Age(
                referenceAge.getName(),

                referenceAge.getEnd(),
                referenceAge.getStart()
        );
    }


    @Test
    public void startDateAndEndDateCanBeTheSame() {
        new Age(
                referenceAge.getName(),

                referenceAge.getStart(),
                referenceAge.getStart()
        );
    }


    @Test
    public void startDateShouldBeIncluded() {
        assertThat(
                referenceAge.includes(
                        referenceAge.getStart()
                ),

                is(true)
        );
    }


    @Test
    public void endDateShouldBeIncluded() {
        assertThat(
                referenceAge.includes(
                        referenceAge.getEnd()
                ),

                is(true)
        );
    }


    @Test
    public void internalDateShouldBeIncluded() {
        LocalDate internalDate =
                referenceAge.getStart().plusDays(1);

        assertThat(
                internalDate.isBefore(referenceAge.getEnd()),
                is(true)
        );

        assertThat(
                referenceAge.includes(
                        internalDate
                ),

                is(true)
        );
    }


    @Test
    public void dateBeforeStartShouldNotBeIncluded() {
        assertThat(
                referenceAge.includes(
                        referenceAge.getStart().minusDays(1)
                ),

                is(false)
        );
    }


    @Test
    public void dateAfterEndShouldNotBeIncluded() {
        assertThat(
                referenceAge.includes(
                        referenceAge.getEnd().plusDays(1)
                ),

                is(false)
        );
    }

    @Test
    public void agesShouldBeSortedFirstByName() {
        Age left =
                new Age(
                        "A",
                        referenceAge.getStart(),
                        referenceAge.getEnd()
                );

        Age right =
                new Age(
                        "B",
                        referenceAge.getStart(),
                        referenceAge.getEnd()
                );

        assertThat(
                left.compareTo(right),
                lessThan(0)
        );
    }


    @Test
    public void agesWithEqualNameShouldBeSortedByStartDate() {
        Age left =
                new Age(
                        referenceAge.getName(),
                        referenceAge.getStart(),
                        referenceAge.getEnd()
                );

        Age right =
                new Age(
                        referenceAge.getName(),
                        referenceAge.getStart().minusDays(1),
                        referenceAge.getEnd()
                );

        assertThat(
                left.compareTo(right),
                greaterThan(0)
        );
    }


    @Test
    public void agesWithEqualNameAndStartDateShouldBeSortedByEndDate() {
        Age left =
                new Age(
                        referenceAge.getName(),
                        referenceAge.getStart(),
                        referenceAge.getEnd()
                );

        Age right =
                new Age(
                        referenceAge.getName(),
                        referenceAge.getStart(),
                        referenceAge.getEnd().minusDays(1)
                );

        assertThat(
                left.compareTo(right),
                greaterThan(0)
        );
    }
}
