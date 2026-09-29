package info.gianlucacosta.historymark.storage;

import info.gianlucacosta.historymark.model.Age;
import info.gianlucacosta.historymark.model.Pin;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface PinRepository {
    Collection<Pin> findAll();

    default Collection<Pin> findAllByAge(Age age) {
        return
                findAll()
                        .stream()
                        .filter(pin ->
                                age.includes(pin.getDate())
                        )
                        .collect(Collectors.toSet());
    }


    default Collection<Pin> findAllByAge(Optional<Age> ageOption) {
        return ageOption
                .map(this::findAllByAge)
                .orElseGet(this::findAll);
    }

    void save(Pin pin);

    void delete(Pin pin);
}
