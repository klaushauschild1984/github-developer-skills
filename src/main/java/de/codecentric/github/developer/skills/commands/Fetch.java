package de.codecentric.github.developer.skills.commands;

import de.codecentric.github.developer.skills.repository.LastUpdate;
import de.codecentric.github.developer.skills.repository.LastUpdateRepository;
import de.codecentric.github.developer.skills.service.FetchService;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@RequiredArgsConstructor
@Component
@CommandLine.Command(
    name = "fetch",
    description = "Fetch developer and skill data and store in data base.",
    subcommands = { CommandLine.HelpCommand.class }
)
class Fetch implements Callable<Integer> {

    @CommandLine.Option(names = "-url", description = "GitHub members url")
    private String membersUrl;

    private final FetchService fetchService;
    private final LastUpdateRepository lastUpdateRepository;

    @Override
    public Integer call() {
        if (membersUrl == null) {
            System.err.println("Members url missing");
            return 1;
        }

        fetchService.fetch(membersUrl);

        lastUpdateRepository.deleteAll();
        lastUpdateRepository.save(LastUpdate.builder().lastUpdated(LocalDateTime.now()).build());

        return 0;
    }
}
