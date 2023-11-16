package de.codecentric.github.developer.skills.commands;

import de.codecentric.github.developer.skills.repository.LastUpdate;
import de.codecentric.github.developer.skills.repository.LastUpdateRepository;
import de.codecentric.github.developer.skills.service.FetchService;
import de.codecentric.github.developer.skills.service.TruncateService;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@RequiredArgsConstructor
@Component
@CommandLine.Command(
    name = "truncate",
    description = "Truncates the data base.",
    subcommands = { CommandLine.HelpCommand.class }
)
class Truncate implements Callable<Integer> {

    private final TruncateService truncateService;

    @Override
    public Integer call() {
        truncateService.perform();
        return 0;
    }
}
