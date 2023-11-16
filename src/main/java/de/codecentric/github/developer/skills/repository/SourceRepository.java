package de.codecentric.github.developer.skills.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceRepository extends JpaRepository<Source, String> {
    default List<Source> findByRepository(de.codecentric.github.developer.skills.repository.Repository repository) {
        return findAll()
            .stream()
            .filter(source -> source.getSource().getRepository().getName().equals(repository.getName()))
            .collect(Collectors.toList());
    }
}
