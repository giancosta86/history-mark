package info.gianlucacosta.historymark;

import info.gianlucacosta.atlas.openstreetmap.tiles.DefaultOpenStreetMapTileUrlFactory;
import info.gianlucacosta.atlas.openstreetmap.tiles.OpenStreetMapTileFactoryInfo;
import info.gianlucacosta.historymark.gui.main.MainFrame;
import info.gianlucacosta.historymark.gui.main.world.WorldTileFactory;
import info.gianlucacosta.historymark.icons.MainIcon;
import info.gianlucacosta.historymark.storage.AgeRepository;
import info.gianlucacosta.historymark.storage.CachingPinRepository;
import info.gianlucacosta.historymark.storage.PinRepository;
import info.gianlucacosta.historymark.storage.db.DbAgeRepository;
import info.gianlucacosta.historymark.storage.db.DbPinRepository;
import info.gianlucacosta.historymark.storage.db.HistorySessionFactoryBuilder;
import info.gianlucacosta.zephyros.db.hsqldb.HyperSQLDatabase;
import info.gianlucacosta.zephyros.os.User;
import info.gianlucacosta.zephyros.swing.dialogs.BasicDialogs;
import info.gianlucacosta.zephyros.swing.dialogs.SplashScreen;
import info.gianlucacosta.zephyros.swing.graphics.Images;
import org.hibernate.SessionFactory;

import javax.swing.Painter;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

class App {
    private static final int MAX_OPEN_STREET_MAP_ZOOM = 10;

    private static final OpenStreetMapTileFactoryInfo defaultTileFactoryInfo =
            new OpenStreetMapTileFactoryInfo(
                    "Stamen - Terrain",
                    "http://tile.stamen.com/terrain",
                    MAX_OPEN_STREET_MAP_ZOOM,
                    Optional.of(
                            "Map tiles by <a href=\"http://stamen.com\">Stamen Design</a>, under <a href=\"http://creativecommons.org/licenses/by/3.0\">CC BY 3.0</a>. Data by <a href=\"http://openstreetmap.org\">OpenStreetMap</a>, under <a href=\"http://www.openstreetmap.org/copyright\">ODbL</a>."
                    )
            );


    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        setupLookAndFeel();

        SplashScreen splashScreen =
                startSplashScreen();


        Path appDirectory =
                setupAppDirectory();

        SessionFactory sessionFactory =
                setupSessionFactory(appDirectory);

        WorldTileFactory worldTileFactory =
                setupWorldTileFactory(appDirectory);

        startMainFrame(
                appDirectory,
                worldTileFactory,
                sessionFactory
        );

        splashScreen.dispose();
    }


    private static void setupLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (Objects.equals(info.getName(), "Nimbus")) {
                    UIManager.setLookAndFeel(
                            info.getClassName()
                    );

                    UIManager.getLookAndFeelDefaults().put(
                            "ProgressBar[Enabled+Indeterminate].backgroundPainter",
                            (Painter<Component>) (g, component, width, height) -> {
                                g.setPaint(Color.WHITE);
                                g.fillRect(0, 0, width, height);
                            }
                    );

                    UIManager.getLookAndFeelDefaults().put(
                            "ProgressBar[Enabled+Indeterminate].foregroundPainter",
                            (Painter<Component>) (g, component, width, height) -> {
                                g.setPaint(Color.BLUE);
                                g.fillOval(5, 5, width - 10, height - 10);
                            }
                    );
                    break;
                }
            }
        } catch (Exception ex) {
            //Just do nothing
        }
    }


    private static SplashScreen startSplashScreen() {
        SplashScreen splashScreen =
                new SplashScreen(
                        Images.readFromURL(
                                MainIcon.getUrl(32)
                        ),

                        ArtifactInfo.getName(),

                        Images.readFromURL(
                                MainIcon.getUrl(512)
                        )
                );

        splashScreen.setVisible(true);

        return splashScreen;
    }


    private static Path setupAppDirectory() {
        Optional<Path> userHomeDirectoryOption =
                User.getHomeDirectory();

        if (!userHomeDirectoryOption.isPresent()) {
            BasicDialogs.showError("Cannot detect the user home directory");
            System.exit(1);
        }

        String majorVersion =
                ArtifactInfo.getVersion().substring(
                        0,
                        ArtifactInfo.getVersion().indexOf(".")
                );


        String majorVersionAppDirComponent =
                majorVersion.equals("1") ?
                        ""
                        :
                        String.format("-%s", majorVersion);

        Path appDirectory =
                userHomeDirectoryOption.get().resolve(
                        String.format(
                                ".%s%s",
                                ArtifactInfo.getName(),
                                majorVersionAppDirComponent
                        )
                );

        try {
            Files.createDirectories(appDirectory);
        } catch (IOException ex) {
            BasicDialogs.showException(ex);

            System.exit(1);
        }


        return appDirectory;
    }


    private static SessionFactory setupSessionFactory(Path appDirectory) {
        Path dbDirectory =
                appDirectory.resolve("db");

        HyperSQLDatabase db =
                new HyperSQLDatabase(dbDirectory);

        SessionFactory sessionFactory =
                new HistorySessionFactoryBuilder(
                        db.getConnectionString()
                )
                        .buildSessionFactory();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Now closing the session factory");
            sessionFactory.close();
        }));

        return sessionFactory;
    }


    private static WorldTileFactory setupWorldTileFactory(Path appDirectory) {
        Path latestTileProviderBaseUrlSettingPath =
                appDirectory.resolve("latestTileProviderBaseURL");


        String latestTileProviderBaseURL;

        try {
            latestTileProviderBaseURL =
                    Files
                            .readAllLines(
                                    latestTileProviderBaseUrlSettingPath
                            )
                            .get(0);
        } catch (Exception ex) {
            latestTileProviderBaseURL =
                    defaultTileFactoryInfo.getBaseURL();
        }


        Optional<String> selectedBaseUrlOption =
                BasicDialogs
                        .askForString(
                                "Please, select a tile provider:",
                                latestTileProviderBaseURL
                        );


        OpenStreetMapTileFactoryInfo tileFactoryInfo =
                selectedBaseUrlOption
                        .map(selectedBaseUrl -> {
                            String normalizedSelectedBaseUrl =
                                    DefaultOpenStreetMapTileUrlFactory
                                            .normalizeBaseURL(
                                                    selectedBaseUrl
                                            );

                            try {
                                Files.write(
                                        latestTileProviderBaseUrlSettingPath,
                                        normalizedSelectedBaseUrl.getBytes()
                                );
                            } catch (Exception ex) {
                                BasicDialogs.showException(ex);
                            }

                            if (Objects.equals(normalizedSelectedBaseUrl, defaultTileFactoryInfo.getBaseURL())) {
                                return defaultTileFactoryInfo;
                            } else {
                                return new OpenStreetMapTileFactoryInfo(
                                        "Custom",
                                        normalizedSelectedBaseUrl,
                                        MAX_OPEN_STREET_MAP_ZOOM,
                                        Optional.empty()
                                );
                            }
                        })
                        .orElse(
                                defaultTileFactoryInfo
                        );

        Path geoCacheRootDirectory =
                appDirectory
                        .resolve("cache")
                        .resolve("maps");


        return new WorldTileFactory(
                tileFactoryInfo,
                geoCacheRootDirectory
        );
    }


    private static void startMainFrame(
            Path appDirectory,
            WorldTileFactory worldTileFactory,
            SessionFactory sessionFactory
    ) {
        AgeRepository ageRepository =
                new DbAgeRepository(sessionFactory);

        PinRepository pinRepository =
                new CachingPinRepository(
                        new DbPinRepository(
                                sessionFactory
                        )
                );

        MainFrame mainFrame = new MainFrame(
                appDirectory,
                worldTileFactory,
                ageRepository,
                pinRepository
        );

        mainFrame.setVisible(true);
    }
}
