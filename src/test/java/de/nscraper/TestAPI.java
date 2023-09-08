package de.nscraper;

import de.nscraper.api.NewsEntry;
import de.nscraper.api.TagesschauAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TestAPI {
    private final TagesschauAPI tagesschauAPI = new TagesschauAPI();

    @Test
    public void testFetchNews() {
        ArrayList<NewsEntry> entries = tagesschauAPI.fetchNews(3, true);
        Assertions.assertNotNull(entries);
    }
}
