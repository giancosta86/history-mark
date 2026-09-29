package info.gianlucacosta.historymark.model;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;


public class PinTest {
    private final Pin referencePin =
            TestObjects.alphaPin;


    @Test(expected = IllegalArgumentException.class)
    public void emptyTitlesShouldNotBeAccepted() {
        new Pin(
                "",
                referencePin.getLocation(),
                referencePin.getDate(),
                referencePin.getEncodedColor(),
                referencePin.getDescription()
        );
    }


    @Test(expected = IllegalArgumentException.class)
    public void titlesHavingOnlySpacesShouldNotBeAccepted() {
        new Pin(
                "  \t      \t  ",
                referencePin.getLocation(),
                referencePin.getDate(),
                referencePin.getEncodedColor(),
                referencePin.getDescription()
        );
    }


    @Test
    public void titlesShouldBeTrimmed() {
        Pin testPin =
                new Pin(
                        "   Example  ",
                        referencePin.getLocation(),
                        referencePin.getDate(),
                        referencePin.getEncodedColor(),
                        referencePin.getDescription()
                );

        assertThat(
                testPin.getTitle(),
                equalTo("Example")
        );
    }

    @Test
    public void emptyDescriptionShouldBePreserved() {
        Pin testPin =
                new Pin(
                        referencePin.getTitle(),
                        referencePin.getLocation(),
                        referencePin.getDate(),
                        referencePin.getEncodedColor(),
                        Optional.empty()
                );

        assertThat(
                testPin.getDescription(),
                equalTo(Optional.empty())
        );
    }


    @Test
    public void descriptionShouldBeTrimmed() {
        Pin testPin =
                new Pin(
                        referencePin.getTitle(),
                        referencePin.getLocation(),
                        referencePin.getDate(),
                        referencePin.getEncodedColor(),
                        Optional.of("    Example  \n  \t   ")
                );

        assertThat(
                testPin.getDescription(),
                equalTo(Optional.of("Example"))
        );
    }


    @Test
    public void descriptionTrimmedToEmptyStringShouldBeReturnedEmpty() {
        Pin testPin =
                new Pin(
                        referencePin.getTitle(),
                        referencePin.getLocation(),
                        referencePin.getDate(),
                        referencePin.getEncodedColor(),
                        Optional.of("     \n  \t   ")
                );

        assertThat(
                testPin.getDescription(),
                equalTo(Optional.empty())
        );
    }
}
