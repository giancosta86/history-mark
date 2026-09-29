package info.gianlucacosta.historymark.storage.xml;

import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public abstract class HistoryWriterReaderTestBase<T> {
    protected abstract T createTestObject();

    protected abstract void writeTestObject(HistoryWriter historyWriter, T testObject);

    protected abstract T readTestObject(HistoryReader historyReader);


    @Test
    public void writingAndThenReadingShouldReturnTheInitialObject() throws Exception {
        StringWriter stringWriter =
                new StringWriter();

        T initialObject =
                createTestObject();


        try (HistoryWriter historyWriter =
                     new HistoryWriter(stringWriter)) {

            writeTestObject(
                    historyWriter,
                    initialObject
            );
        }


        String xmlString =
                stringWriter.toString();


        System.out.println("\n---------------------");
        System.out.println(xmlString);
        System.out.println("---------------------\n");


        try (HistoryReader historyReader =
                     new HistoryReader(
                             new StringReader(
                                     xmlString
                             )
                     )
        ) {
            T retrievedObject =
                    readTestObject(historyReader);

            assertThat(
                    retrievedObject,
                    equalTo(initialObject)
            );
        }
    }
}
