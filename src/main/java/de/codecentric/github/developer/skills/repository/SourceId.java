package de.codecentric.github.developer.skills.repository;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Embeddable
@NoArgsConstructor
@SuperBuilder
@Getter
public class SourceId implements Serializable {

    @OneToOne
    private Repository repository;

    private String language;
}
