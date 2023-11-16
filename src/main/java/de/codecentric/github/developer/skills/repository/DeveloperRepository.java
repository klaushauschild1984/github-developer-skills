package de.codecentric.github.developer.skills.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, String> {
    List<Developer> findByLoginStartsWith(String login);
    List<Developer> findAllByOrderByLoginAsc();
}
