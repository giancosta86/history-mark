package info.gianlucacosta.historymark.gui.main.ages;

import info.gianlucacosta.historymark.gui.util.HistoryObjectDialog;
import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.zephyros.swing.components.LabeledInputPanel;
import info.gianlucacosta.zephyros.swing.components.PlainLocalDatePicker;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.UUID;


class AgeDialog extends HistoryObjectDialog<Age> {
    private JTextField nameField;
    private PlainLocalDatePicker startPicker;
    private PlainLocalDatePicker endPicker;


    public AgeDialog() {
        super();
    }

    public AgeDialog(Age ageToEdit) {
        super(ageToEdit);
    }


    @Override
    protected JPanel createContentPanel() {
        LabeledInputPanel contentPanel =
                new LabeledInputPanel();

        contentPanel.addLabel("Name:");

        nameField =
                new JTextField() {
                    {
                        setColumns(20);
                    }
                };

        contentPanel.addInput(nameField);


        contentPanel.addLabel("Start:");

        startPicker =
                new PlainLocalDatePicker();

        contentPanel.addInput(startPicker);


        contentPanel.addLabel("End:");

        endPicker =
                new PlainLocalDatePicker();

        contentPanel.addInput(endPicker);


        return contentPanel;
    }


    @Override
    protected String getDialogTitle(boolean hasInitialValue) {
        return hasInitialValue ?
                "Edit age..."
                :
                "Add age...";
    }

    @Override
    protected void writeToGuiFields(Age initialValue) {
        nameField.setText(initialValue.getName());

        startPicker.setDate(initialValue.getStart());

        endPicker.setDate(initialValue.getEnd());
    }

    @Override
    protected Age readFromGuiFields() {
        return new Age(
                initialValueOption
                        .map(Age::getId)
                        .orElseGet(UUID::randomUUID),

                nameField.getText(),
                startPicker.getDate(),
                endPicker.getDate()
        );
    }
}
