package info.gianlucacosta.historymark.gui.main;

import info.gianlucacosta.historymark.gui.util.IconButton;
import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.historymark.storage.AgeRepository;
import info.gianlucacosta.historymark.storage.PinRepository;
import info.gianlucacosta.historymark.storage.db.DuplicateException;
import info.gianlucacosta.historymark.storage.xml.HistoryReader;
import info.gianlucacosta.historymark.storage.xml.HistoryWriter;
import info.gianlucacosta.zephyros.swing.dialogs.BasicDialogs;

import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ImportExportPanel extends JPanel {
    private static final FileNameExtensionFilter AGE_FILE_FILTER =
            new FileNameExtensionFilter("Ages XML file", "ages");

    private static final FileNameExtensionFilter PIN_FILE_FILTER =
            new FileNameExtensionFilter("Pins XML file", "pins");


    private final AgeRepository ageRepository;
    private final PinRepository pinRepository;
    private final Supplier<Optional<Age>> currentAgeOptionSupplier;

    private final Consumer<Age> savedAgeConsumer;
    private final Consumer<Pin> savedPinConsumer;

    public ImportExportPanel(
            AgeRepository ageRepository,
            PinRepository pinRepository,
            Supplier<Optional<Age>> currentAgeOptionSupplier,
            Consumer<Age> savedAgeConsumer,
            Consumer<Pin> savedPinConsumer) {
        this.ageRepository = ageRepository;
        this.pinRepository = pinRepository;
        this.currentAgeOptionSupplier = currentAgeOptionSupplier;
        this.savedAgeConsumer = savedAgeConsumer;

        this.savedPinConsumer = savedPinConsumer;

        initGUI();
    }


    private void initGUI() {
        setLayout(new FlowLayout());

        add(
                new IconButton(
                        "Export ages...",
                        "exportAges.png",
                        this::exportAges
                )
        );


        add(
                new IconButton(
                        "Import ages...",
                        "importAges.png",
                        this::importAges
                )
        );


        add(
                new IconButton(
                        "Export pins...",
                        "exportPins.png",
                        this::exportPins
                )
        );


        add(
                new IconButton(
                        "Import pins...",
                        "importPins.png",
                        this::importPins
                )
        );
    }


    private void exportAges(ActionEvent event) {
        List<Age> ages =
                ageRepository.findAll();

        if (ages.isEmpty()) {
            BasicDialogs.showInfo("No age has been defined.");
            return;
        }

        Optional<Path> targetPathOption =
                BasicDialogs.saveFile(
                        "Export ages...",
                        AGE_FILE_FILTER
                );

        targetPathOption.ifPresent(targetPath -> {
            try (HistoryWriter writer =
                         new HistoryWriter(
                                 Files.newBufferedWriter(targetPath)
                         )
            ) {
                writer.writeAges(ages);

                BasicDialogs.showInfo("Ages exported successfully.");
            } catch (Exception ex) {
                BasicDialogs.showException(ex);
            }
        });
    }


    private void importAges(ActionEvent event) {
        Optional<Path> sourcePathOption =
                BasicDialogs.openFile(
                        "Import ages...",
                        AGE_FILE_FILTER
                );


        sourcePathOption.ifPresent(sourcePath -> {
            try (
                    HistoryReader reader =
                            new HistoryReader(
                                    Files.newBufferedReader(sourcePath)
                            )
            ) {
                List<Age> retrievedAges =
                        reader.readAges();

                final AtomicInteger successImports =
                        new AtomicInteger(0);


                retrievedAges
                        .forEach(age -> {
                            try {
                                ageRepository.save(age);

                                savedAgeConsumer.accept(age);

                                successImports.incrementAndGet();
                            } catch (DuplicateException ex) {
                                System.err.println(
                                        String.format(
                                                "---> Conflicting age '%s'",
                                                age.getName()
                                        )
                                );

                                System.err.println();
                            } catch (Exception ex) {
                                System.err.println(
                                        String.format(
                                                "---> Error while importing '%s'",
                                                age.getName()
                                        )
                                );

                                ex.printStackTrace(System.err);
                                System.err.println();
                            }
                        });


                if (successImports.get() == 0) {
                    BasicDialogs.showWarning("No age could be imported.\n\nPlease, check the console log for details.");
                } else {
                    BasicDialogs.showInfo(
                            String.format(
                                    "Imported/overwritten ages: %s out of %s.",
                                    successImports.get(),
                                    retrievedAges.size()
                            )
                    );
                }
            } catch (Exception ex) {
                BasicDialogs.showException(ex);
            }
        });
    }


    private void exportPins(ActionEvent event) {
        Optional<Age> currentAgeOption =
                currentAgeOptionSupplier.get();

        Collection<Pin> pins =
                pinRepository.findAllByAge(currentAgeOption);

        if (pins.isEmpty()) {
            if (currentAgeOption.isPresent()) {
                BasicDialogs.showInfo("No pins are associated to the selected age.");
            } else {
                BasicDialogs.showInfo("There are no pins on the map.");
            }

            return;
        }

        Optional<Path> targetPathOption =
                BasicDialogs.saveFile(
                        "Export pins...",
                        PIN_FILE_FILTER
                );

        targetPathOption.ifPresent(targetPath -> {
            try (HistoryWriter writer =
                         new HistoryWriter(
                                 Files.newBufferedWriter(targetPath)
                         )) {
                writer.writePins(pins);

                BasicDialogs.showInfo("Pins exported successfully.");
            } catch (Exception ex) {
                BasicDialogs.showException(ex);
            }
        });
    }


    private void importPins(ActionEvent event) {
        Optional<Path> sourcePathOption =
                BasicDialogs.openFile(
                        "Import pins...",
                        PIN_FILE_FILTER
                );

        sourcePathOption.ifPresent(sourcePath -> {
            try (HistoryReader reader =
                         new HistoryReader(
                                 Files.newBufferedReader(sourcePath)
                         )) {
                Collection<Pin> retrievedPins =
                        reader.readPins();

                final AtomicInteger successImports =
                        new AtomicInteger(0);

                retrievedPins.forEach(pin -> {
                    try {
                        pinRepository.save(pin);

                        savedPinConsumer.accept(pin);

                        successImports.incrementAndGet();
                    } catch (DuplicateException ex) {
                        System.err.println(
                                String.format(
                                        "---> Conflicting pin '%s'",
                                        pin.getTitle()
                                )
                        );

                        System.err.println();
                    } catch (Exception ex) {
                        System.err.println(
                                String.format(
                                        "---> Error while importing '%s'",
                                        pin.getTitle()
                                )
                        );

                        ex.printStackTrace(System.err);
                        System.err.println();
                    }
                });

                if (successImports.get() == 0) {
                    BasicDialogs.showWarning("No pin could be imported.\n\nPlease, check the console log for details.");
                } else {
                    BasicDialogs.showInfo(
                            String.format(
                                    "Imported/overwritten pins: %s out of %s.",
                                    successImports.get(),
                                    retrievedPins.size()
                            )
                    );
                }
            } catch (Exception ex) {
                BasicDialogs.showException(ex);
            }
        });
    }
}
