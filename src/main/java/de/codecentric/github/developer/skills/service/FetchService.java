package de.codecentric.github.developer.skills.service;

import de.codecentric.github.developer.skills.repository.Developer;
import de.codecentric.github.developer.skills.repository.DeveloperRepository;
import de.codecentric.github.developer.skills.repository.DeveloperSource;
import de.codecentric.github.developer.skills.repository.DeveloperSourceId;
import de.codecentric.github.developer.skills.repository.DeveloperSourceRepository;
import de.codecentric.github.developer.skills.repository.Repository;
import de.codecentric.github.developer.skills.repository.RepositoryRepository;
import de.codecentric.github.developer.skills.repository.Source;
import de.codecentric.github.developer.skills.repository.SourceId;
import de.codecentric.github.developer.skills.repository.SourceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FetchService {

    private final DeveloperRepository developerRepository;
    private final RepositoryRepository repositoryRepository;
    private final SourceRepository sourceRepository;
    private final DeveloperSourceRepository developerSourceRepository;
    private final String token;

    public FetchService(
        final DeveloperRepository developerRepository,
        final RepositoryRepository repositoryRepository,
        final SourceRepository sourceRepository,
        final DeveloperSourceRepository developerSourceRepository,
        @Value("${commands.fetch.token}") final String token
    ) {
        this.developerRepository = developerRepository;
        this.repositoryRepository = repositoryRepository;
        this.sourceRepository = sourceRepository;
        this.developerSourceRepository = developerSourceRepository;
        this.token = token;
    }

    public void fetch(final String membersUrl) {
        final Map<String, List<WebDeveloper>> webDevelopers = fetchDevelopers(membersUrl);
        System.out.println(String.format("Fetching %s developers...", webDevelopers.size()));
        final List<Developer> developers = persistDevelopers(webDevelopers);
        developers.forEach(developer -> {
            System.out.println(String.format("  processing %s", developer.getLogin()));
            fetchRepositories(developer, webDevelopers.get(developer.getLogin()).get(0).getRepositoriesUrl());
            analyseDeveloper(developer);
        });
    }

    private Map<String, List<WebDeveloper>> fetchDevelopers(String membersUrl) {
        final ResponseEntity<List<WebDeveloper>> developersResponse = exchange(
            membersUrl,
            new ParameterizedTypeReference<>() {}
        );
        return developersResponse.getBody().stream().collect(Collectors.groupingBy(WebDeveloper::getLogin));
    }

    private List<Developer> persistDevelopers(final Map<String, List<WebDeveloper>> webDevelopers) {
        final List<Developer> developers = webDevelopers
            .values()
            .stream()
            .map(developer -> Developer.builder().login(developer.get(0).getLogin()).build())
            .collect(Collectors.toList());
        return developerRepository.saveAll(developers);
    }

    private void fetchRepositories(final Developer developer, final String repositoriesUrl) {
        final ResponseEntity<List<WebRepository>> repositoriesResponse = exchange(
            repositoriesUrl,
            new ParameterizedTypeReference<>() {}
        );
        final Map<String, List<WebRepository>> webRepositories = repositoriesResponse
            .getBody()
            .stream()
            .collect(Collectors.groupingBy(WebRepository::getName));

        final List<Repository> repositories = persistRepositories(webRepositories);
        developer.getRepositories().addAll(repositories);
        developerRepository.save(developer);

        repositories.forEach(repository ->
            fetchSources(repository, webRepositories.get(repository.getName()).get(0).getLanguages())
        );
    }

    private List<Repository> persistRepositories(final Map<String, List<WebRepository>> webRepositories) {
        final List<Repository> repositories = webRepositories
            .values()
            .stream()
            .map(webRepository -> Repository.builder().name(webRepository.get(0).getName()).build())
            .collect(Collectors.toList());
        return repositoryRepository.saveAll(repositories);
    }

    private void fetchSources(final Repository repository, final String languagesUrl) {
        final ResponseEntity<Map<String, Long>> sourcesResponse = exchange(
            languagesUrl,
            new ParameterizedTypeReference<>() {}
        );
        persistSources(repository, sourcesResponse.getBody());
    }

    private void persistSources(final Repository repository, final Map<String, Long> webSources) {
        final List<Source> sources = webSources
            .entrySet()
            .stream()
            .map(webSource -> {
                return Source
                    .builder()
                    .source(SourceId.builder().repository(repository).language(webSource.getKey()).build())
                    .linesOfCode(webSource.getValue())
                    .build();
            })
            .collect(Collectors.toList());
        sourceRepository.saveAll(sources);
    }

    private void analyseDeveloper(final Developer developer) {
        final Map<String, Long> languages = new HashMap<>();
        developer
            .getRepositories()
            .forEach(repository -> {
                sourceRepository
                    .findByRepository(repository)
                    .forEach(source -> {
                        final String language = source.getSource().getLanguage();
                        final long count = languages.getOrDefault(language, 0L);
                        languages.put(language, count + 1);
                    });
            });
        final List<DeveloperSource> developerSources = languages
            .entrySet()
            .stream()
            .map(entry ->
                DeveloperSource
                    .builder()
                    .developerSource(DeveloperSourceId.builder().developer(developer).language(entry.getKey()).build())
                    .numberOfProjects(entry.getValue())
                    .build()
            )
            .collect(Collectors.toList());
        developerSourceRepository.saveAll(developerSources);
    }

    private <T> ResponseEntity<T> exchange(final String url, final ParameterizedTypeReference<T> typeReference) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new RestTemplate().exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), typeReference);
    }
}
