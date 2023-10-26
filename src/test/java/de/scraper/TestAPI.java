package de.scraper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class TestAPI {
    private final TagesschauAPI api = new TagesschauAPI();

    @Test
    public void testFetchNews() {
        ArrayList<NewsEntry> entries = api.fetchNews(7, false);
        Assertions.assertNotNull(entries);

        api.sortByTags(entries);

        try (FileWriter writer = new FileWriter("news.txt")) {
            Assertions.assertNotNull(writer);
            for (NewsEntry entry : entries) {
                Assertions.assertNotNull(entry);
                Assertions.assertNotNull(entry.getTitle());
                Assertions.assertNotNull(entry.getDate());
                Assertions.assertNotNull(entry.getShareURL());

                writer.append(entry.getTitle()).append("\n");
                writer.append(entry.getDate()).append("\n");
                writer.append(entry.getShareURL()).append("\n");
                writer.append("Tags: ");
                for (int i = 0; i < entry.getTags().size(); ++i) {
                    NewsEntry.NewsTag tag = entry.getTags().get(i);
                    Assertions.assertNotNull(tag);
                    Assertions.assertNotNull(tag.getValue());

                    writer.append(tag.getValue()).append(":").append(String.valueOf(tag.getFrequency()));

                    if (i != entry.getTags().size() - 1) {
                        writer.append(", ");
                    } else {
                        writer.append("\n").append("\n");
                    }
                }
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
