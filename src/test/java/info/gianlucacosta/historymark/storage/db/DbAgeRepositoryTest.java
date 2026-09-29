package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.historymark.model.Age;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;

import java.time.LocalDate;
import java.util.UUID;

import static info.gianlucacosta.historymark.model.TestObjects.alphaAge;
import static info.gianlucacosta.historymark.model.TestObjects.betaAge;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class DbAgeRepositoryTest extends DbTestBase {
    private DbAgeRepository ageRepository;


    @Override
    public void setup() {
        super.setup();

        ageRepository =
                new DbAgeRepository(sessionFactory);
    }


    @Test
    public void saveShouldWorkForNewInstances() {
        ageRepository.save(alphaAge);
        ageRepository.save(betaAge);

        try (Session session = sessionFactory.openSession()) {
            Query query =
                    session.createQuery(
                            "SELECT COUNT (*) FROM Age"
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
                ageRepository.findAll(),
                is(empty())
        );
    }


    @Test
    public void retrieveShouldReturnSortedItems() {
        ageRepository.save(betaAge);
        ageRepository.save(alphaAge);


        assertThat(
                ageRepository.findAll(),

                contains(alphaAge, betaAge)
        );
    }


    @Test
    public void saveShouldWorkOnEditedInstance() {
        ageRepository.save(alphaAge);
        ageRepository.save(betaAge);

        Age newAlphaAge =
                new Age(
                        alphaAge.getId(),
                        "Edited age",
                        LocalDate.of(betaAge.getStart().getYear() + 100, 1, 1),
                        LocalDate.of(betaAge.getEnd().getYear() + 100, 1, 1)
                );

        assertThat(
                betaAge.getStart().isBefore(newAlphaAge.getStart()),
                is(true)
        );


        ageRepository.save(newAlphaAge);

        assertThat(
                ageRepository.findAll(),

                contains(betaAge, newAlphaAge)
        );
    }


    @Test
    public void deleteShouldWork() {
        ageRepository.save(alphaAge);
        ageRepository.save(betaAge);

        ageRepository.delete(betaAge);

        assertThat(
                ageRepository.findAll(),
                contains(alphaAge)
        );
    }


    @Test(expected = DuplicateException.class)
    public void savingDistinctAgesHavingTheSameNameShouldFail() {
        Age cloneAge =
                new Age(
                        UUID.randomUUID(),
                        alphaAge.getName(),
                        alphaAge.getStart(),
                        alphaAge.getEnd()
                );


        ageRepository.save(alphaAge);
        ageRepository.save(cloneAge);
    }
}
