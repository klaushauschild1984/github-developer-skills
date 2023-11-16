package de.codecentric.github.developer.skills;

import org.junit.jupiter.api.Test;

class ApplicationTest {

    @Test
    void blank() {
        Application.main(new String[] {});
    }

    @Test
    void help() {
        Application.main(new String[] { "help" });
    }

    @Test
    void helpFetch() {
        Application.main(new String[] { "help", "fetch" });
    }

    @Test
    void helpTruncate() {
        Application.main(new String[] { "help", "truncate" });
    }

    @Test
    void fetchWithoutUrl() {
        Application.main(new String[] { "fetch" });
    }

    @Test
    void fetch() {
        Application.main(new String[] { "fetch", "-url", "https://api.github.com/orgs/codecentric/members" });
    }

    @Test
    void truncate() {
        Application.main(new String[] { "truncate" });
    }
}
