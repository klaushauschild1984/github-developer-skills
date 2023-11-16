package de.codecentric.github.developer.skills.repository;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
public class LastUpdate {

    @Id
    private LocalDateTime lastUpdated;
}
