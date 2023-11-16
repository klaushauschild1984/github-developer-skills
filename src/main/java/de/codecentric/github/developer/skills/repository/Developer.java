package de.codecentric.github.developer.skills.repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
public class Developer {

    @Id
    private String email;
}
