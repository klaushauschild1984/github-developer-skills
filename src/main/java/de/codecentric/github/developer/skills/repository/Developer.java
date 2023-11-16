package de.codecentric.github.developer.skills.repository;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
public class Developer {

    @Id
    private String login;

    @ManyToMany
    private Set<Repository> repositories;
}
