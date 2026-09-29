package info.gianlucacosta.historymark.gui.main;

import info.gianlucacosta.historymark.ArtifactInfo;
import info.gianlucacosta.historymark.gui.main.ages.AgePanel;
import info.gianlucacosta.historymark.gui.main.world.WorldMap;
import info.gianlucacosta.historymark.gui.main.world.WorldTileFactory;
import info.gianlucacosta.historymark.gui.util.GeoPositionDialog;
import info.gianlucacosta.historymark.gui.util.IconButton;
import info.gianlucacosta.historymark.icons.MainIcon;
import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Pin;
import info.gianlucacosta.historymark.storage.AgeRepository;
import info.gianlucacosta.historymark.storage.PinRepository;
import info.gianlucacosta.historymark.storage.db.DuplicateException;
import info.gianlucacosta.zephyros.files.FileBasedActions;
import info.gianlucacosta.zephyros.swing.dialogs.BasicDialogs;
import info.gianlucacosta.zephyros.swing.graphics.Images;
import info.gianlucacosta.zephyros.swing.graphics.Screenshots;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

public class MainFrame extends JFrame {
    private static final FileNameExtensionFilter SCREENSHOT_FILE_FILTER =
            new FileNameExtensionFilter("JPEG image", "jpg");

    private final PinRepository pinRepository;

    private final WorldMap worldMap;

    private JLabel positionLabel;

    private AgePanel agePanel;


    public MainFrame(
            Path appDirectory,
            WorldTileFactory worldTileFactory,
            AgeRepository ageRepository,
            PinRepository pinRepository
    ) {
        this.pinRepository = pinRepository;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setIconImage(
                Images.readFromURL(MainIcon.getUrl(32))
        );


        setTitle(ArtifactInfo.getName());

        setLayout(new BorderLayout());


        worldMap = new WorldMap(
                worldTileFactory,
                this::handleMouseGeoPositionChanged,
                this::handleInteractiveAddPinRequest,
                this::handleInteractiveEditPinRequest,
                this::handleInteractiveRemovePinRequest
        ) {
            {
                setPreferredSize(
                        new Dimension(700, 550)
                );
            }
        };

        add(worldMap, BorderLayout.CENTER);


        add(
                createToolbar(
                        ageRepository
                ),

                BorderLayout.NORTH
        );


        JPanel statusPanel =
                createStatusPanel();


        add(
                statusPanel,
                BorderLayout.SOUTH
        );

        pack();

        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                tryToShowWelcomeMessage(appDirectory);

                worldMap.requestFocus();
            }
        });
    }


    private JPanel createToolbar(
            AgeRepository ageRepository
    ) {
        JPanel toolbar =
                new JPanel();

        toolbar.setLayout(
                new FlowLayout()
        );

        agePanel = new AgePanel(
                ageRepository,
                this::handleAgeSelectionChange
        );

        toolbar.add(agePanel);

        toolbar.add(
                new JSeparator(SwingConstants.VERTICAL)
        );


        toolbar.add(
                new IconButton(
                        "Go to position...",
                        "goToPosition.png",

                        this::askToGoToPosition
                )
        );


        toolbar.add(
                new JSeparator(SwingConstants.VERTICAL)
        );


        toolbar.add(
                new IconButton(
                        "Export map as image...",
                        "screenshot.png",

                        this::askToExportMapAsImage
                )
        );


        toolbar.add(
                new JSeparator(SwingConstants.VERTICAL)
        );


        toolbar.add(
                new ImportExportPanel(
                        ageRepository,
                        pinRepository,
                        agePanel::getSelectedAge,
                        this::handleAgeImported,
                        this::handlePinImported
                )
        );


        toolbar.add(
                new JSeparator(SwingConstants.VERTICAL)
        );


        toolbar.add(
                new AboutPanel()
        );

        return toolbar;
    }


    private void handleAgeSelectionChange(Optional<Age> currentAgeOption) {
        Collection<Pin> pins =
                pinRepository.findAllByAge(currentAgeOption);

        worldMap.setPins(pins);
    }


    private void handleAgeImported(Age age) {
        agePanel.ensureAge(age);
    }

    private void handlePinImported(Pin pin) {
        if (canDrawPin(pin)) {
            worldMap.ensurePin(pin);
        }
    }


    private JPanel createStatusPanel() {
        JPanel statusPanel =
                new JPanel() {
                    {
                        setLayout(new BorderLayout());

                        setFont(
                                getFont()
                                        .deriveFont(14.0f)
                        );

                        setBorder(
                                BorderFactory.createEmptyBorder(7, 7, 7, 7)
                        );
                    }
                };


        positionLabel =
                new JLabel(" ");


        statusPanel.add(
                positionLabel,
                BorderLayout.WEST
        );


        return statusPanel;
    }


    private void handleMouseGeoPositionChanged(GeoPosition position) {
        positionLabel.setText(
                String.format(
                        "Latitude: %.3f    Longitude: %.3f",
                        position.getLatitude(),
                        position.getLongitude()
                )
        );
    }


    private void handleInteractiveAddPinRequest(Pin pin) {
        try {
            pinRepository.save(pin);

            if (canDrawPin(pin)) {
                worldMap.ensurePin(pin);
            } else {
                showFilteredOutPinMessage();
            }
        } catch (DuplicateException ex) {
            BasicDialogs.showWarning("Cannot add the pin, because it already exists.");
        } catch (Exception ex) {
            BasicDialogs.showException(ex);
        }
    }


    private boolean canDrawPin(Pin pin) {
        return
                agePanel.getSelectedAge()
                        .map(currentAge ->
                                currentAge.includes(pin.getDate())
                        )
                        .orElse(true);

    }


    private void showFilteredOutPinMessage() {
        BasicDialogs.showInfo("The pin won't be visible now, as its date is not in the range of the selected age.");
    }


    private void handleInteractiveEditPinRequest(Pin pin) {
        try {
            pinRepository.save(pin);

            if (canDrawPin(pin)) {
                worldMap.ensurePin(pin);
            } else {
                worldMap.removePin(pin);
                showFilteredOutPinMessage();
            }
        } catch (DuplicateException ex) {
            BasicDialogs.showWarning("The requested changes duplicate an existing pin.");
        } catch (Exception ex) {
            BasicDialogs.showException(ex);
        }
    }


    private void handleInteractiveRemovePinRequest(Pin pin) {
        try {
            pinRepository.delete(pin);

            worldMap.removePin(pin);
        } catch (Exception ex) {
            BasicDialogs.showException(ex);
        }
    }


    private void askToGoToPosition(ActionEvent event) {
        GeoPosition currentPosition =
                worldMap.getCenterPosition();

        GeoPositionDialog geoPositionDialog =
                new GeoPositionDialog(
                        currentPosition
                );


        Optional<GeoPosition> selectedPositionOption =
                geoPositionDialog.show();

        selectedPositionOption.ifPresent(worldMap::setCenterPosition);
    }


    private void askToExportMapAsImage(ActionEvent event) {
        Optional<Path> outputPathOption =
                BasicDialogs.saveFile(
                        "Save screenshot...",
                        SCREENSHOT_FILE_FILTER
                );

        outputPathOption.ifPresent(outputPath -> {
            try {
                Screenshots.saveComponentAsJpeg(worldMap, outputPath);

                BasicDialogs.showInfo("Screenshot saved successfully.");
            } catch (Exception ex) {
                BasicDialogs.showException(ex);
            }
        });
    }


    private void tryToShowWelcomeMessage(Path appDirectory) {
        FileBasedActions.runOnce(
                appDirectory.resolve("firstTimeMessageShown"),

                () -> {
                    try {
                        Path welcomeMessagePath =
                                Paths.get(
                                        getClass()
                                                .getResource("WelcomeMessage.htm")
                                                .toURI()
                                );

                        byte[] welcomeMessageBytes =
                                Files.readAllBytes(welcomeMessagePath);

                        String welcomeMessage =
                                new String(welcomeMessageBytes);

                        BasicDialogs.showInfo(welcomeMessage);
                    } catch (URISyntaxException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
    }
}
