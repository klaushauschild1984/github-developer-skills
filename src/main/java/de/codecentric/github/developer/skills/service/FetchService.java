package de.codecentric.github.developer.skills.service;

import de.codecentric.github.developer.skills.repository.Developer;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FetchService {

    public List<Developer> fetch(final String gitHubMemberUrl) {
        final ResponseEntity<List<WebDeveloper>> developersResponse = new RestTemplate()
            .exchange(gitHubMemberUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        developersResponse
            .getBody()
            .stream()
            .map(webDeveloper -> {
                final ResponseEntity<List<WebRepository>> repositoriesResponse = new RestTemplate()
                    .exchange(
                        webDeveloper.getRepositoriesUrl(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                    );

                repositoriesResponse
                    .getBody()
                    .stream()
                    .map(webRepository -> {
                        final ResponseEntity<Map<String, Integer>> languagesResponse = new RestTemplate()
                            .exchange(
                                webDeveloper.getRepositoriesUrl(),
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<>() {}
                            );

                        return null;
                    });

                return null;
            });

        return null;
    }
}
