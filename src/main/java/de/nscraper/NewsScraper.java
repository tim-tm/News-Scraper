package de.nscraper;

import de.nscraper.api.NewsEntry;
import de.nscraper.api.TagesschauAPI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <a href="https://tagesschau.api.bund.dev/">TagesschauAPI documentation</a>
 * @author David, Tim
 */
public class NewsScraper {
    private static final TagesschauAPI api = new TagesschauAPI();

    public static void main(String[] args) throws IOException {
        ArrayList<NewsEntry> entries = api.fetchNews(7, true);
        FileWriter writer = new FileWriter("news.txt");
        for (NewsEntry entry : entries) {
            writer.append(entry.getTitle()).append("\n");
            writer.append(entry.getDate()).append("\n");
            writer.append(entry.getShareURL()).append("\n").append("\n");
        }
        writer.flush();
        writer.close();
    }
}
