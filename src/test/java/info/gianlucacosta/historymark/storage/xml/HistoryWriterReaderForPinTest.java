package info.gianlucacosta.historymark.storage.xml;

import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.historymark.model.TestObjects;

import java.util.Collection;

public class HistoryWriterReaderForPinTest extends HistoryWriterReaderTestBase<Collection<Pin>> {
    @Override
    protected Collection<Pin> createTestObject() {
        return TestObjects.pins;
    }

    @Override
    protected void writeTestObject(HistoryWriter historyWriter, Collection<Pin> testPins) {
        historyWriter.writePins(testPins);
    }

    @Override
    protected Collection<Pin> readTestObject(HistoryReader historyReader) {
        return historyReader.readPins();
    }
}
