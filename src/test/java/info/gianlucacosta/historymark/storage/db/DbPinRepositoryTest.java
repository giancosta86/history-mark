package info.gianlucacosta.historymark.storage.db;

public class DbPinRepositoryTest extends DbPinRepositoryTestBase<DbPinRepository> {
    @Override
    protected DbPinRepository createPinRepository() {
        return new DbPinRepository(sessionFactory);
    }
}
