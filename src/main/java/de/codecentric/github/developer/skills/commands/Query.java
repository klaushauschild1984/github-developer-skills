package de.codecentric.github.developer.skills.commands;

import de.codecentric.github.developer.skills.repository.Developer;
import de.codecentric.github.developer.skills.repository.DeveloperSource;
import de.codecentric.github.developer.skills.repository.DeveloperSourceRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
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

    private final DeveloperSourceRepository developerSourceRepository;

    @Override
    public Integer call() {
        final Map<Developer, List<DeveloperSource>> developers = getDevelopers();
        if (restrictedLanguage != null) {
            developers
                .values()
                .stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(DeveloperSource::getNumberOfProjects).reversed())
                .forEach(developerSource ->
                    printDeveloper(developerSource.getDeveloperSource().getDeveloper(), List.of(developerSource))
                );
        } else {
            developers.forEach(Query::printDeveloper);
        }
        return 0;
    }

    private static void printDeveloper(final Developer developer, final List<DeveloperSource> sources) {
        System.out.println(String.format("- %s", developer.getLogin()));
        sources.forEach(developerSource -> {
            System.out.println(
                String.format(
                    "    %s: %s",
                    developerSource.getDeveloperSource().getLanguage(),
                    developerSource.getNumberOfProjects()
                )
            );
        });
        System.out.println();
    }

    private Map<Developer, List<DeveloperSource>> getDevelopers() {
        if (restrictedDeveloper != null) {
            return developerSourceRepository
                .findAllByOrderByNumberOfProjectsDesc()
                .stream()
                .filter(developerSource ->
                    developerSource.getDeveloperSource().getDeveloper().getLogin().equals(restrictedDeveloper)
                )
                .sorted(
                    Comparator.comparing(developerSource ->
                        developerSource.getDeveloperSource().getDeveloper().getLogin()
                    )
                )
                .collect(Collectors.groupingBy(developerSource -> developerSource.getDeveloperSource().getDeveloper()));
        }
        if (restrictedLanguage != null) {
            return developerSourceRepository
                .findAllByOrderByNumberOfProjectsDesc()
                .stream()
                .filter(developerSource -> developerSource.getDeveloperSource().getLanguage().equals(restrictedLanguage)
                )
                .collect(Collectors.groupingBy(developerSource -> developerSource.getDeveloperSource().getDeveloper()));
        }
        return developerSourceRepository
            .findAllByOrderByNumberOfProjectsDesc()
            .stream()
            .sorted(
                Comparator.comparing(developerSource -> developerSource.getDeveloperSource().getDeveloper().getLogin())
            )
            .collect(Collectors.groupingBy(developerSource -> developerSource.getDeveloperSource().getDeveloper()));
    }
}
