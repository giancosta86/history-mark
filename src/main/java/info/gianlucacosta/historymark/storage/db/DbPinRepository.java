package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.historymark.storage.PinRepository;
import info.gianlucacosta.zephyros.db.hibernate.FunctionalSession;
import info.gianlucacosta.zephyros.db.hibernate.PersistenceExceptions;
import org.hibernate.SessionFactory;

import javax.persistence.PersistenceException;
import java.util.Collection;


public class DbPinRepository extends FunctionalSession implements PinRepository {
    public DbPinRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Collection<Pin> findAll() {
        return runNamedListQuery(
                "info.gianlucacosta.historymark.ReadPins",
                Pin.class
        );
    }

    @Override
    public void save(Pin pin) {
        try {
            saveOrUpdateInTransaction(pin);
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
    public void delete(Pin pin) {
        deleteInTransaction(pin);
    }
}
