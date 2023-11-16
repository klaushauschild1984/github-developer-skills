package de.codecentric.github.developer.skills.service;

import de.codecentric.github.developer.skills.repository.Developer;
import de.codecentric.github.developer.skills.repository.DeveloperRepository;
import de.codecentric.github.developer.skills.repository.Repository;
import de.codecentric.github.developer.skills.repository.RepositoryRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class FetchService {

    private final DeveloperRepository developerRepository;
    private final RepositoryRepository repositoryRepository;

    public void fetch(final String membersUrl) {
        final ResponseEntity<List<WebDeveloper>> developersResponse = new RestTemplate()
            .exchange(membersUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        final List<Developer> developers = developersResponse
            .getBody()
            .stream()
            .map(webDeveloper -> Developer.builder().login(webDeveloper.getLogin()).build())
            .collect(Collectors.toList());
        developerRepository.saveAll(developers);

        developersResponse
            .getBody()
            .forEach(webDeveloper -> {
                final ResponseEntity<List<WebRepository>> repositoriesResponse = new RestTemplate()
                    .exchange(
                        webDeveloper.getRepositoriesUrl(),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                    );

                final List<Repository> repositories = repositoriesResponse
                    .getBody()
                    .stream()
                    .map(webRepository -> {
                        final Repository repository = repositoryRepository
                            .findByName(webRepository.getName())
                            .orElseGet(() -> Repository.builder().name(webRepository.getName()).build());
                        repository
                            .getDevelopers()
                            .add(
                                developers
                                    .stream()
                                    .filter(developer -> developer.getLogin().equals(webDeveloper.getLogin()))
                                    .findFirst()
                                    .get()
                            );

                        return repository;
                    })
                    .collect(Collectors.toList());
                repositoryRepository.saveAll(repositories);
                //                        repositoriesResponse
                //                            .getBody()
                //                            .stream()
                //                            .map(webRepository -> {
                //                                final ResponseEntity<Map<String, Integer>> languagesResponse = new RestTemplate()
                //                                    .exchange(
                //                                        webDeveloper.getRepositoriesUrl(),
                //                                        HttpMethod.GET,
                //                                        null,
                //                                        new ParameterizedTypeReference<>() {}
                //                                    );
                //
                //                                return null;
                //                            });

            });
    }
}
