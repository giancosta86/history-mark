package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.historymark.storage.CachingPinRepository;

public class CachingPinRepositoryTest extends DbPinRepositoryTestBase<CachingPinRepository> {
    @Override
    protected CachingPinRepository createPinRepository() {
        return new CachingPinRepository(
                new DbPinRepository(
                        sessionFactory
                )
        );
    }
}
