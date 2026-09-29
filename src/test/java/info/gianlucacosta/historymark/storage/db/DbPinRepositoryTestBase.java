package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.historymark.model.Location;
import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.historymark.storage.PinRepository;
import info.gianlucacosta.zephyros.swing.graphics.Colors;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;

import java.awt.Color;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static info.gianlucacosta.historymark.model.TestObjects.alphaPin;
import static info.gianlucacosta.historymark.model.TestObjects.betaPin;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public abstract class DbPinRepositoryTestBase<T extends PinRepository> extends DbTestBase {
    protected T pinRepository;

    protected abstract T createPinRepository();


    @Override
    public void setup() {
        super.setup();

        pinRepository =
                createPinRepository();
    }


    @Test
    public void saveShouldWorkForNewInstances() {
        pinRepository.save(alphaPin);
        pinRepository.save(betaPin);

        try (Session session = sessionFactory.openSession()) {
            Query query =
                    session.createQuery(
                            "SELECT COUNT (*) FROM Pin"
                    );

            Long count =
                    (Long) query.getSingleResult();

            assertThat(
                    count,
                    equalTo(2L)
            );
        }
    }


    @Test
    public void findAllShouldWorkOnEmptyDb() {
        assertThat(
                pinRepository.findAll(),
                is(empty())
        );
    }


    @Test
    public void retrieveShouldReturnItems() {
        pinRepository.save(alphaPin);
        pinRepository.save(betaPin);


        assertThat(
                pinRepository.findAll(),

                containsInAnyOrder(alphaPin, betaPin)
        );
    }


    @Test
    public void saveShouldWorkOnEditedInstance() {
        pinRepository.save(alphaPin);
        pinRepository.save(betaPin);

        Pin newAlphaPin =
                new Pin(
                        alphaPin.getId(),
                        "Edited pin",
                        new Location(90, 90),
                        LocalDate.of(alphaPin.getDate().getYear() + 100, 1, 1),
                        Colors.encode(Color.RED),
                        Optional.of("Brand-new description")
                );

        pinRepository.save(newAlphaPin);

        assertThat(
                pinRepository.findAll(),

                containsInAnyOrder(betaPin, newAlphaPin)
        );
    }


    @Test
    public void deleteShouldWork() {
        pinRepository.save(alphaPin);
        pinRepository.save(betaPin);

        pinRepository.delete(alphaPin);

        assertThat(
                pinRepository.findAll(),
                contains(betaPin)
        );
    }


    @Test(expected = DuplicateException.class)
    public void savingDistinctPinsHavingTheSameDataShouldFail() {
        Pin clonePin =
                new Pin(
                        UUID.randomUUID(),
                        alphaPin.getTitle(),
                        alphaPin.getLocation(),
                        alphaPin.getDate(),
                        alphaPin.getEncodedColor(),
                        alphaPin.getDescription()
                );

        pinRepository.save(alphaPin);
        pinRepository.save(clonePin);
    }
}
