package de.codecentric.github.developer.skills.commands;

import de.codecentric.github.developer.skills.service.FetchService;
import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "fetch")
class Fetch implements Callable<Integer> {

    @CommandLine.Option(names = "-url", description = "GitHub members url")
    private String membersUrl;

    @Autowired
    private FetchService fetchService;

    @Override
    public Integer call() {
        fetchService.fetch(membersUrl);

        return 0;
    }
}
