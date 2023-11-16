package de.codecentric.github.developer.skills.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
class WebRepository {

    private String name;
    @JsonProperty("languages_url")
    private String languages;
}
