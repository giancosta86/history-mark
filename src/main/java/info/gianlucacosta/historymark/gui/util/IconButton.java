package info.gianlucacosta.historymark.gui.util;

import info.gianlucacosta.historymark.gui.main.MainFrame;
import info.gianlucacosta.zephyros.swing.graphics.Images;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.Optional;


public class IconButton extends JButton {
    public IconButton(String toolTip, String iconFileName) {
        this(toolTip, iconFileName, Optional.empty());
    }


    public IconButton(String toolTip, String iconFileName, ActionListener onClick) {
        this(toolTip, iconFileName, Optional.of(onClick));
    }


    public IconButton(String toolTip, String iconFileName, Optional<ActionListener> onClickOption) {
        setToolTipText(toolTip);

        Image buttonIcon =
                Images.readFromURL(
                        MainFrame.class.getResource("icons/" + iconFileName)
                );

        setIcon(
                new ImageIcon(buttonIcon)
        );

        onClickOption.ifPresent(this::addActionListener);
    }
}
