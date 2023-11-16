package de.codecentric.github.developer.skills.commands;

import java.util.concurrent.Callable;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Component
@Command(name = "github-developer-skills", subcommands = { CommandLine.HelpCommand.class, Fetch.class })
public class GitHubDeveloperSkills implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Use sub command 'help' for further details.");
        return 0;
    }
    //    @Component
    //    @Command(name = "subsub", exitCodeOnExecutionException = 44)
    //    static class SubSub implements Callable<Integer> {
    //
    //        @Option(names = "-z", description = "optional option")
    //        private String z;
    //
    //        //        @Autowired
    //        //        private SomeService service;
    //
    //        @Override
    //        public Integer call() {
    //            System.out.printf("mycommand sub subsub was called with -z=%s. Service says: '%s'%n", z);
    //            return 43;
    //        }
    //    }
}
