package de.codecentric.github.developer.skills.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryRepository
    extends JpaRepository<de.codecentric.github.developer.skills.repository.Repository, String> {}
