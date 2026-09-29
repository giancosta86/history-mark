package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.storage.AgeRepository;
import info.gianlucacosta.zephyros.db.hibernate.FunctionalSession;
import info.gianlucacosta.zephyros.db.hibernate.PersistenceExceptions;
import org.hibernate.SessionFactory;

import javax.persistence.PersistenceException;
import java.util.List;

public class DbAgeRepository extends FunctionalSession implements AgeRepository {
    public DbAgeRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    @Override
    public List<Age> findAll() {
        return runNamedListQuery(
                "info.gianlucacosta.historymark.ReadAges",
                Age.class
        );
    }


    @Override
    public void save(Age age) {
        try {
            saveOrUpdateInTransaction(age);
        } catch (PersistenceException ex) {
            PersistenceExceptions.getSqlState(ex).ifPresent(sqlState -> {
                switch (sqlState) {
                    case UNIQUE_VIOLATION:
                        throw new DuplicateException();
                }
            });

            throw ex;
        }
    }


    @Override
    public void delete(Age age) {
        deleteInTransaction(age);
    }
}
