package de.codecentric.github.developer.skills.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
class WebDeveloper {

    private String login;
    @JsonProperty("repos_url")
    private String repositoriesUrl;
}
