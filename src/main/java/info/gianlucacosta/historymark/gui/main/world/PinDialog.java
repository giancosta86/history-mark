package info.gianlucacosta.historymark.gui.main.world;

import info.gianlucacosta.historymark.gui.util.HistoryObjectDialog;
import info.gianlucacosta.historymark.gui.util.LocationEditor;
import info.gianlucacosta.historymark.model.Location;
import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.zephyros.swing.components.ColorPicker;
import info.gianlucacosta.zephyros.swing.components.LabeledInputPanel;
import info.gianlucacosta.zephyros.swing.components.PlainLocalDatePicker;
import info.gianlucacosta.zephyros.swing.graphics.Colors;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Optional;
import java.util.UUID;

class PinDialog extends HistoryObjectDialog<Pin> {
    private static final Color DEFAULT_PIN_COLOR =
            Color.ORANGE;

    private final Location pinLocation;

    private JTextField titleField;
    private PlainLocalDatePicker datePicker;
    private LocationEditor locationEditor;
    private ColorPicker colorPicker;
    private JTextArea descriptionArea;


    public PinDialog(Location pinLocation) {
        super();

        this.pinLocation =
                pinLocation;
    }


    public PinDialog(Pin pinToEdit) {
        super(pinToEdit);

        pinLocation =
                pinToEdit.getLocation();
    }


    @Override
    protected JPanel createContentPanel() {
        LabeledInputPanel contentPanel =
                new LabeledInputPanel();


        contentPanel.addLabel("Title:");

        titleField =
                new JTextField(30);

        contentPanel.addInput(titleField);


        contentPanel.addLabel("Date:");

        datePicker =
                new PlainLocalDatePicker();

        contentPanel.addInput(datePicker);


        contentPanel.addLabel("Location:");

        locationEditor =
                new LocationEditor();

        locationEditor.setLocationValue(pinLocation);

        contentPanel.addInput(locationEditor);


        contentPanel.addLabel("Color:");

        colorPicker =
                new ColorPicker(
                        new Dimension(
                                220,
                                26
                        )
                ) {
                    {
                        setColor(DEFAULT_PIN_COLOR);
                    }
                };

        contentPanel.addInput(colorPicker);


        contentPanel.addLabel("Description:");

        descriptionArea =
                new JTextArea(10, 20) {
                    {
                        setLineWrap(true);
                    }
                };

        contentPanel.addInput(
                new JScrollPane(
                        descriptionArea
                ),

                constraints -> {
                    constraints.weighty = 1;
                }
        );


        return contentPanel;
    }


    @Override
    protected String getDialogTitle(boolean hasInitialValue) {
        return hasInitialValue ?
                "Edit pin..."
                :
                "Add pin...";
    }

    @Override
    protected void writeToGuiFields(Pin initialValue) {
        titleField.setText(initialValue.getTitle());

        datePicker.setDate(initialValue.getDate());

        colorPicker.setColor(
                Color.decode(
                        initialValue.getEncodedColor()
                )
        );

        descriptionArea.setText(
                initialValue
                        .getDescription()
                        .orElse("")
        );
    }


    @Override
    protected Pin readFromGuiFields() {
        return new Pin(
                initialValueOption
                        .map(Pin::getId)
                        .orElseGet(UUID::randomUUID),

                titleField.getText(),
                locationEditor.getLocationValue(),
                datePicker.getDate(),
                Colors.encode(colorPicker.getColor()),

                Optional.of(descriptionArea.getText())
        );
    }
}
