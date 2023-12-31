package de.codecentric.github.developer.skills.commands;

import de.codecentric.github.developer.skills.repository.DeveloperRepository;
import de.codecentric.github.developer.skills.repository.LastUpdate;
import de.codecentric.github.developer.skills.repository.LastUpdateRepository;
import de.codecentric.github.developer.skills.repository.RepositoryRepository;
import de.codecentric.github.developer.skills.repository.Source;
import de.codecentric.github.developer.skills.repository.SourceRepository;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@RequiredArgsConstructor
@Component
@Command(
    name = "github-developer-skills",
    subcommands = { CommandLine.HelpCommand.class, Fetch.class, Truncate.class, Query.class }
)
public class GitHubDeveloperSkills implements Callable<Integer> {

    private final DeveloperRepository developerRepository;
    private final RepositoryRepository repositoryRepository;
    private final SourceRepository sourceRepository;
    private final LastUpdateRepository lastUpdateRepository;

    @Override
    public Integer call() {
        System.out.println("# github-developer-skills #");
        System.out.println();
        System.out.println("  data:");
        System.out.println(String.format("    developers:         %s", developerRepository.count()));
        System.out.println(String.format("    repositories:       %s", repositoryRepository.count()));
        System.out.println(
            String.format(
                "    languages:          %s",
                sourceRepository.findAll().stream().map(source -> source.getSource().getLanguage()).distinct().count()
            )
        );
        System.out.println(
            String.format(
                "    lines of code:      %s",
                sourceRepository.findAll().stream().mapToLong(Source::getLinesOfCode).sum()
            )
        );
        System.out.println(
            String.format(
                "    last updated:       %s",
                lastUpdateRepository.findFirstBy().map(LastUpdate::getLastUpdated).orElse(null)
            )
        );
        System.out.println();
        System.out.println("Use sub command 'help' for further details.");
        return 0;
    }
}
