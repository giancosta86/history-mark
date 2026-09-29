package info.gianlucacosta.historymark.storage;

import info.gianlucacosta.historymark.model.Pin;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CachingPinRepository implements PinRepository {
    private final PinRepository backingRepository;
    private final Map<UUID, Pin> cache;


    public CachingPinRepository(PinRepository backingRepository) {
        this.backingRepository = backingRepository;

        this.cache =
                backingRepository
                        .findAll()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Pin::getId,
                                        Function.identity()
                                )
                        );
    }


    @Override
    public Collection<Pin> findAll() {
        return cache.values();
    }

    @Override
    public void save(Pin pin) {
        backingRepository.save(pin);

        cache.put(
                pin.getId(),
                pin
        );
    }

    @Override
    public void delete(Pin pin) {
        backingRepository.delete(pin);

        cache.remove(pin.getId());
    }
}
