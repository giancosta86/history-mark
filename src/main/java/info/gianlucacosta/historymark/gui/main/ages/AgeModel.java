package info.gianlucacosta.historymark.gui.main.ages;

import info.gianlucacosta.historymark.model.Age;

import javax.swing.DefaultComboBoxModel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;

class AgeModel extends DefaultComboBoxModel<AgeItem> {
    private final TreeSet<AgeItem> ageItemSet =
            new TreeSet<>();

    public AgeModel(List<Age> ages) {
        Stream.concat(
                Stream.of(AgeItem.ALL_TIME),

                ages
                        .stream()
                        .map(AgeItem::new)
        )
                .forEach(this::addElement);
    }


    @Override
    public void addElement(AgeItem ageItem) {
        ageItemSet.add(ageItem);

        int itemIndex =
                ageItemSet
                        .headSet(ageItem)
                        .size();

        super.insertElementAt(
                ageItem,
                itemIndex
        );
    }


    @Override
    public void removeElement(Object ageItem) {
        ageItemSet.remove(ageItem);

        super.removeElement(ageItem);
    }


    public void ensureElement(AgeItem item) {
        item.getAge().ifPresent(newAge -> {
            Optional<AgeItem> matchingAgeItemOption =
                    ageItemSet
                            .stream()
                            .filter(ageItem ->
                                    ageItem
                                            .getAge()
                                            .map(age ->
                                                    Objects.equals(
                                                            age.getId(),
                                                            newAge.getId()
                                                    )
                                            )
                                            .orElse(false)

                            )
                            .findAny();


            matchingAgeItemOption.ifPresent(this::removeElement);
        });


        addElement(item);
    }
}
