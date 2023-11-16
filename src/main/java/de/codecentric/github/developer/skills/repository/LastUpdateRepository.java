package de.codecentric.github.developer.skills.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastUpdateRepository extends JpaRepository<LastUpdate, LocalDateTime> {
    Optional<LastUpdate> findFirstBy();
}
