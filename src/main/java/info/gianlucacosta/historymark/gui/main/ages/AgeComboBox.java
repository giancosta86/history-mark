package info.gianlucacosta.historymark.gui.main.ages;

import javax.swing.JComboBox;

class AgeComboBox extends JComboBox<AgeItem> {
    public AgeComboBox(AgeModel model) {
        super(model);

        setSelectedItem(AgeItem.ALL_TIME);
    }
}
