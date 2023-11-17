package de.codecentric.github.developer.skills.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperSourceRepository extends JpaRepository<DeveloperSource, String> {
    List<DeveloperSource> findAllByOrderByNumberOfProjectsDesc();
}
