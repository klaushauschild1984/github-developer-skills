package de.codecentric.github.developer.skills;

import de.codecentric.github.developer.skills.commands.GitHubDeveloperSkills;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@Component
public class ApplicationRunner implements CommandLineRunner, ExitCodeGenerator {

    private final GitHubDeveloperSkills gitHubDeveloperSkills;

    private final IFactory factory;

    private int exitCode;

    public ApplicationRunner(GitHubDeveloperSkills gitHubDeveloperSkills, IFactory factory) {
        this.gitHubDeveloperSkills = gitHubDeveloperSkills;
        this.factory = factory;
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine commandLine = new CommandLine(gitHubDeveloperSkills, factory);
        exitCode = commandLine.execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
