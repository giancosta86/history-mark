package info.gianlucacosta.historymark.storage.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Pin;

class HistoryXStream extends XStream {
    private static final HistoryXStream instance =
            new HistoryXStream();

    public static HistoryXStream getInstance() {
        return instance;
    }


    private HistoryXStream() {
        initSecurity();
        initConverters();
        initAliases();
    }


    private void initSecurity() {
        addPermission(NoTypePermission.NONE);

        addPermission(NullPermission.NULL);
        addPermission(PrimitiveTypePermission.PRIMITIVES);

        allowTypesByWildcard(new String[]{
                "info.gianlucacosta.historymark.**",
                "java.util.**",
                "java.time.**"
        });
    }

    private void initConverters() {
        registerConverter(new LocalDateConverter());
    }


    private void initAliases() {
        alias("ages", AgeBundle.class);
        addImplicitCollection(AgeBundle.class, "ages");
        alias("age", Age.class);

        alias("pins", PinBundle.class);
        addImplicitCollection(PinBundle.class, "pins");
        alias("pin", Pin.class);
    }
}
