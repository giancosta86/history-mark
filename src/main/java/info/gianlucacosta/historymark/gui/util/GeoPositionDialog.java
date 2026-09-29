package info.gianlucacosta.historymark.gui.util;

import info.gianlucacosta.historymark.model.Location;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class GeoPositionDialog extends HistoryObjectDialog<GeoPosition> {
    private LocationEditor locationEditor;

    public GeoPositionDialog() {
        super();
    }


    public GeoPositionDialog(GeoPosition valueToedit) {
        super(valueToedit);
    }


    @Override
    protected String getDialogTitle(boolean hasInitialValue) {
        return "Set position...";
    }


    @Override
    protected JPanel createContentPanel() {
        locationEditor =
                new LocationEditor();

        locationEditor.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        return locationEditor;
    }


    @Override
    protected void writeToGuiFields(GeoPosition initialValue) {
        locationEditor.setLocationValue(
                new Location(
                        initialValue
                )
        );
    }


    @Override
    protected GeoPosition readFromGuiFields() {
        return locationEditor
                .getLocationValue()
                .toPosition();
    }
}
