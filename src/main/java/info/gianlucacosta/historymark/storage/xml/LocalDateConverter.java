package info.gianlucacosta.historymark.storage.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.time.LocalDate;

/**
 * Converts a LocalDate by writing its year, month and day as attributes
 */
class LocalDateConverter implements Converter {
    private static final String YEAR_ATTRIBUTE = "year";
    private static final String MONTH_ATTRIBUTE = "month";
    private static final String DAY_ATTRIBUTE = "day";


    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        LocalDate sourceDate =
                (LocalDate) source;

        writer.addAttribute(
                YEAR_ATTRIBUTE,
                String.valueOf(sourceDate.getYear())
        );

        writer.addAttribute(
                MONTH_ATTRIBUTE,
                String.valueOf(sourceDate.getMonthValue())
        );


        writer.addAttribute(
                DAY_ATTRIBUTE,
                String.valueOf(sourceDate.getDayOfMonth())
        );
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        int year =
                Integer.parseInt(reader.getAttribute(YEAR_ATTRIBUTE));

        int month =
                Integer.parseInt(reader.getAttribute(MONTH_ATTRIBUTE));

        int day =
                Integer.parseInt(reader.getAttribute(DAY_ATTRIBUTE));


        return LocalDate.of(
                year,
                month,
                day
        );
    }

    @Override
    public boolean canConvert(Class type) {
        return type == LocalDate.class;
    }
}
