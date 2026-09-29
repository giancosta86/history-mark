package info.gianlucacosta.historymark.storage.xml;

import info.gianlucacosta.historymark.model.Pin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class PinBundle {
    private final Set<Pin> pins;

    public PinBundle(Collection<Pin> pins) {
        this.pins = new HashSet<>(pins);
    }

    public Collection<Pin> getPins() {
        return pins;
    }
}
