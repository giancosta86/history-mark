package info.gianlucacosta.historymark.storage;

import info.gianlucacosta.historymark.model.Age;

import java.util.List;

public interface AgeRepository {
    List<Age> findAll();

    void save(Age age);

    void delete(Age age);
}
