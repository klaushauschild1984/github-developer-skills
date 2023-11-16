package de.codecentric.github.developer.skills.repository;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Fetch;
import org.springframework.context.annotation.Lazy;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
public class Repository {

    @Id
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @Default
    private Set<Developer> developers = new HashSet<>();
}
