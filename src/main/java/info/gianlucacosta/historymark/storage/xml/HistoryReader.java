package info.gianlucacosta.historymark.storage.xml;

import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Pin;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

public class HistoryReader implements AutoCloseable {
    private final Reader source;

    public HistoryReader(Reader source) {
        this.source = source;
    }

    public List<Age> readAges() {
        AgeBundle ageBundle =
                readObject();

        return ageBundle.getAges();
    }


    public Collection<Pin> readPins() {
        PinBundle pinBundle =
                readObject();

        return pinBundle.getPins();
    }

    @SuppressWarnings("unchecked")
    private <T> T readObject() {
        return (T) HistoryXStream.getInstance().fromXML(source);
    }


    @Override
    public void close() throws IOException {
        source.close();
    }
}
