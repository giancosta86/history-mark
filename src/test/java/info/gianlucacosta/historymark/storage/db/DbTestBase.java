package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.zephyros.db.hsqldb.HyperSQLDatabase;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;

public abstract class DbTestBase {
    protected SessionFactory sessionFactory;

    @Before
    public void setup() {
        HyperSQLDatabase db =
                new HyperSQLDatabase();

        HistorySessionFactoryBuilder sessionFactoryBuilder =
                new HistorySessionFactoryBuilder(
                        db.getConnectionString()
                );

        sessionFactory =
                sessionFactoryBuilder.buildSessionFactory();
    }


    @After
    public void cleanup() {
        sessionFactory.close();
    }
}
