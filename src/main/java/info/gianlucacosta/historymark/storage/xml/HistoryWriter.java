package info.gianlucacosta.historymark.storage.xml;

import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Pin;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

public class HistoryWriter implements AutoCloseable {
    private final Writer target;

    public HistoryWriter(Writer target) {
        this.target = target;
    }


    public void writeAges(List<Age> ages) {
        writeObject(new AgeBundle(ages));
    }


    public void writePins(Collection<Pin> pins) {
        writeObject(new PinBundle(pins));
    }


    private void writeObject(Object object) {
        PrettyPrintWriter prettyPrintWriter =
                new PrettyPrintWriter(target);

        HistoryXStream.getInstance().marshal(object, prettyPrintWriter);
    }


    @Override
    public void close() throws IOException {
        target.close();
    }
}
