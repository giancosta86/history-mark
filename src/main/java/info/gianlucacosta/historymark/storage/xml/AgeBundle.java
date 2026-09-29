package info.gianlucacosta.historymark.storage.xml;

import info.gianlucacosta.historymark.model.Age;

import java.util.ArrayList;
import java.util.List;

class AgeBundle {
    private final List<Age> ages;

    public AgeBundle(List<Age> ages) {
        this.ages = new ArrayList<>(ages);
    }

    public List<Age> getAges() {
        return ages;
    }
}
