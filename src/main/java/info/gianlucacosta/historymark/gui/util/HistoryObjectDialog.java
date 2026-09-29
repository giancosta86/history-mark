package info.gianlucacosta.historymark.gui.util;

import info.gianlucacosta.historymark.icons.MainIcon;
import info.gianlucacosta.zephyros.swing.dialogs.ObjectDialog;
import info.gianlucacosta.zephyros.swing.graphics.Images;

import java.awt.Image;
import java.util.Optional;

public abstract class HistoryObjectDialog<T> extends ObjectDialog<T> {
    public HistoryObjectDialog() {
    }

    public HistoryObjectDialog(T valueToedit) {
        super(valueToedit);
    }

    @Override
    protected Optional<Image> getWindowIcon() {
        return Optional.of(
                Images.readFromURL(
                        MainIcon.getUrl(32)
                )
        );
    }
}
