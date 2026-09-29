package info.gianlucacosta.historymark.gui.main.ages;

import info.gianlucacosta.historymark.gui.util.IconButton;
import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.storage.AgeRepository;
import info.gianlucacosta.historymark.storage.db.DuplicateException;
import info.gianlucacosta.zephyros.swing.dialogs.BasicDialogs;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class AgePanel extends JPanel {
    private final AgeModel ageModel;
    private final AgeComboBox ageComboBox;
    private final JButton editAgeButton;
    private final JButton removeAgeButton;
    private final Consumer<Optional<Age>> onCurrentAgeChanged;

    public AgePanel(
            AgeRepository ageRepository,
            Consumer<Optional<Age>> onCurrentAgeChanged
    ) {
        this.onCurrentAgeChanged = onCurrentAgeChanged;

        List<Age> initialAges =
                ageRepository.findAll();

        ageModel =
                new AgeModel(initialAges);

        setLayout(new FlowLayout());

        add(new JLabel("Ages:"));

        ageComboBox = new AgeComboBox(ageModel) {
            {
                addItemListener(AgePanel.this::handleAgeComboBoxItemEvent);

                setPreferredSize(new Dimension(400, 40));

                setFont(
                        getFont().deriveFont(14.0f)
                );
            }
        };


        JButton addAgeButton =
                new IconButton(
                        "Add new age...",
                        "addAge.png",

                        event -> {
                            AgeDialog ageDialog =
                                    new AgeDialog();

                            Optional<Age> ageOption =
                                    ageDialog.show();

                            ageOption.ifPresent(age -> {
                                try {
                                    ageRepository.save(age);

                                    AgeItem ageItem =
                                            new AgeItem(age);

                                    ageModel.addElement(ageItem);

                                    ageComboBox.setSelectedItem(ageItem);
                                } catch (DuplicateException ex) {
                                    BasicDialogs.showWarning("Cannot add the age, because it already exists.");
                                } catch (Exception ex) {
                                    BasicDialogs.showException(ex);
                                }
                            });
                        }
                );


        editAgeButton =
                new IconButton(
                        "Edit selected age...",
                        "editAge.png",

                        event -> {
                            AgeItem selectedAgeItem =
                                    (AgeItem) ageComboBox.getSelectedItem();

                            Age selectedAge =
                                    selectedAgeItem.getAge().get();

                            AgeDialog ageDialog =
                                    new AgeDialog(selectedAge);

                            Optional<Age> updatedAgeOption =
                                    ageDialog.show();

                            updatedAgeOption.ifPresent(updatedAge -> {
                                try {
                                    ageRepository.save(updatedAge);

                                    AgeItem updatedAgeItem =
                                            new AgeItem(updatedAge);

                                    ageModel.ensureElement(updatedAgeItem);

                                    ageComboBox.setSelectedItem(updatedAgeItem);
                                } catch (DuplicateException ex) {
                                    BasicDialogs.showWarning("The requested changes duplicate an existing age.");
                                } catch (Exception ex) {
                                    BasicDialogs.showException(ex);
                                }
                            });
                        });


        removeAgeButton =
                new IconButton(
                        "Remove selected age",
                        "removeAge.png",

                        event -> {
                            try {
                                AgeItem selectedAgeItem =
                                        (AgeItem) ageComboBox.getSelectedItem();

                                Age selectedAge =
                                        selectedAgeItem.getAge().get();

                                ageRepository.delete(selectedAge);

                                ageModel.removeElement(selectedAgeItem);
                            } catch (Exception ex) {
                                BasicDialogs.showException(ex);
                            }
                        });


        add(ageComboBox);

        add(addAgeButton);
        add(editAgeButton);
        add(removeAgeButton);

        handleAgeComboBoxItemEvent(
                new ItemEvent(
                        ageComboBox,
                        ItemEvent.ITEM_FIRST,
                        AgeItem.ALL_TIME,
                        ItemEvent.SELECTED
                )
        );
    }


    private void handleAgeComboBoxItemEvent(ItemEvent event) {
        if (event.getStateChange() != ItemEvent.SELECTED) {
            return;
        }

        updateAgeButtons();

        AgeItem ageItem =
                (AgeItem) event.getItem();

        onCurrentAgeChanged.accept(ageItem.getAge());
    }


    private void updateAgeButtons() {
        editAgeButton.setEnabled(
                !Objects.equals(
                        ageComboBox.getSelectedItem(),
                        AgeItem.ALL_TIME
                )
        );

        removeAgeButton.setEnabled(
                editAgeButton.isEnabled()
        );
    }


    public Optional<Age> getSelectedAge() {
        AgeItem selectedAgeItem =
                (AgeItem) ageComboBox.getSelectedItem();

        if (selectedAgeItem == null) {
            return Optional.empty();
        }

        return selectedAgeItem.getAge();
    }


    public void ensureAge(Age age) {
        AgeItem ageItem =
                new AgeItem(age);

        ageModel.ensureElement(ageItem);
    }
}
