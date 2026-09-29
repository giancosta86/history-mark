package info.gianlucacosta.historymark.storage.xml;

import info.gianlucacosta.historymark.model.Age;

import java.util.List;

import static info.gianlucacosta.historymark.model.TestObjects.ages;

public class HistoryWriterReaderForAgeTest extends HistoryWriterReaderTestBase<List<Age>> {
    @Override
    protected List<Age> createTestObject() {
        return ages;
    }

    @Override
    protected void writeTestObject(HistoryWriter historyWriter, List<Age> testAges) {
        historyWriter.writeAges(testAges);
    }

    @Override
    protected List<Age> readTestObject(HistoryReader historyReader) {
        return historyReader.readAges();
    }
}
