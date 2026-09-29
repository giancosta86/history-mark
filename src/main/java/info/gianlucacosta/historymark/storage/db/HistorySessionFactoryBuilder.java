package info.gianlucacosta.historymark.storage.db;

import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Location;
import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.zephyros.db.hibernate.SessionFactoryBuilder;

public class HistorySessionFactoryBuilder extends SessionFactoryBuilder {
    public HistorySessionFactoryBuilder(String connectionString) {
        super(connectionString);

        addAnnotatedClasses(
                Age.class,
                Pin.class,
                Location.class
        );


        addResources(
                getClass().getResource("Queries.hbm.xml")
        );
    }
}
