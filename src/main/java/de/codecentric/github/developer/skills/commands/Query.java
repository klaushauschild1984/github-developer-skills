package de.codecentric.github.developer.skills.commands;

import de.codecentric.github.developer.skills.repository.Developer;
import de.codecentric.github.developer.skills.repository.DeveloperRepository;
import de.codecentric.github.developer.skills.repository.SourceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@RequiredArgsConstructor
@Component
@CommandLine.Command(
    name = "query",
    description = "Query data base with various criteria.",
    subcommands = { CommandLine.HelpCommand.class }
)
class Query implements Callable<Integer> {

    @CommandLine.Option(names = "-developer", description = "Restrict output to a specific developer")
    private String restrictedDeveloper;

    @CommandLine.Option(names = "-language", description = "Restrict output to a specific language")
    private String restrictedLanguage;

    private final DeveloperRepository developerRepository;
    private final SourceRepository sourceRepository;

    @Override
    public Integer call() {
        final List<Developer> developers = getDevelopers();
        developers.forEach(developer -> {
            System.out.println(String.format("- %s", developer.getLogin()));
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
            languages
                .entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> {
                    System.out.println(String.format("    %s: %s", entry.getKey(), entry.getValue()));
                });
            System.out.println();
        });
        return 0;
    }

    private List<Developer> getDevelopers() {
        if (restrictedDeveloper != null) {
            return developerRepository.findByLoginStartsWith(restrictedDeveloper);
        }
        return developerRepository.findAllByOrderByLoginAsc();
    }
}
