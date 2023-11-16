package de.codecentric.github.developer.skills.repository;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Builder.Default;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @Default
    private Set<Repository> repositories = new HashSet<>();
}
