package info.gianlucacosta.historymark.gui.main;

import info.gianlucacosta.historymark.ArtifactInfo;
import info.gianlucacosta.historymark.gui.util.IconButton;
import info.gianlucacosta.historymark.icons.MainIcon;
import info.gianlucacosta.zephyros.os.User;
import info.gianlucacosta.zephyros.swing.dialogs.AboutBox;
import info.gianlucacosta.zephyros.swing.graphics.Images;

import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Optional;

class AboutPanel extends JPanel {
    public AboutPanel() {
        setLayout(new FlowLayout());

        add(
                new IconButton(
                        "Visit website",
                        "web.png",
                        this::showWebsite
                )
        );


        add(
                new IconButton(
                        "About...",
                        "about.png",
                        this::showAboutBox
                )
        );
    }


    private void showWebsite(ActionEvent event) {
        User.openBrowser(
                ArtifactInfo.getWebsite()
        );
    }


    private void showAboutBox(ActionEvent event) {
        AboutBox aboutBox =
                new AboutBox(
                        Images.readFromURL(
                                MainIcon.getUrl(32)
                        ),

                        Images.readFromURL(
                                MainIcon.getUrl(128)
                        ),
                        ArtifactInfo.getName(),
                        ArtifactInfo.getVersion(),
                        ArtifactInfo.getCopyrightHolder()
                );


        aboutBox.setVisible(true);
    }
}
