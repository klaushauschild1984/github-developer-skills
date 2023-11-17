package de.codecentric.github.developer.skills.repository;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
public class DeveloperSource {

    @EmbeddedId
    private DeveloperSourceId developerSource;

    private long numberOfProjects;
}
