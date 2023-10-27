package de.scraper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * This is a simple interface to easily use the <a href="https://tagesschau.api.bund.dev/">TagesschauAPIv2</a>.
 *
 * @author Tim
 * @since 0.0.1
 */
public class TagesschauAPI {
    private static final String TS_ENDPOINT = "https://www.tagesschau.de/api2u";
    private static final String TS_NEWS = TS_ENDPOINT + "/news";

    private final HttpClient client;

    /**
     * Simple constructor, no parameters. Will still need to be called since there are no static methods.
     */
    public TagesschauAPI() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Fetch the news using the Tagesschau APIv2
     *
     * @param date         Date in yyMMdd format.
     * @param onlyBreaking Should only breaking-news be listed?
     * @return List of News that were fetched.
     */
    public ArrayList<NewsEntry> fetchNews(String date, boolean onlyBreaking) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(buildNewsURL(date)))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        String res;
        try {
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 300) {
                throw new RuntimeException("Bad status code: " + response.statusCode());
            }
            res = response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return parseResponse(res, onlyBreaking);
    }

    /**
     * Fetch the daily news using the Tagesschau APIv2
     *
     * @param onlyBreaking Should only breaking-news be listed?
     * @return List of News that were fetched.
     */
    public ArrayList<NewsEntry> fetchNews(boolean onlyBreaking) {
        return fetchNews(formatCurrentTime(), onlyBreaking);
    }

    /**
     * Fetch news from a time until now using the Tagesschau APIv2
     *
     * @param days         Days to go back.
     * @param onlyBreaking Should only breaking-news be listed?
     * @return List of News that were fetched.
     */
    public ArrayList<NewsEntry> fetchNews(int days, boolean onlyBreaking) {
        ArrayList<NewsEntry> entries = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            String date = formatTime(LocalDateTime.now().minusDays(i));
            System.out.println(date);
            entries.addAll(fetchNews(date, onlyBreaking));
        }
        return entries;
    }

    /**
     * Parse the incoming response from the TagesschauAPI.
     *
     * @param res          Response body
     * @param onlyBreaking Should only breaking-news be listed?
     * @return List of NewsEntries containing the important information.
     */
    private ArrayList<NewsEntry> parseResponse(String res, boolean onlyBreaking) {
        ArrayList<NewsEntry> entries = new ArrayList<>();
        JSONObject object = new JSONObject(res);
        JSONArray news = object.getJSONArray("news");

        for (int i = 0; i < news.length(); i++) {
            JSONObject obj = news.getJSONObject(i);
            if (obj.has("type") && obj.getString("type").equals("video")) continue;

            String title = obj.getString("title");
            if (onlyBreaking && !title.contains("+")) continue;
            NewsEntry entry = new NewsEntry(title, obj.getString("date"), obj.getString("shareURL"));

            if (obj.has("tags")) {
                JSONArray tags = obj.getJSONArray("tags");

                for (int j = 0; j < tags.length(); j++) {
                    String value = tags.getJSONObject(j).getString("tag");
                    entry.addTag(new NewsEntry.NewsTag(value));
                }
            }
            entries.add(entry);
        }

        computeFrequency(entries);
        return entries;
    }

    /**
     * Compute how often the tag appears in all tags for every tag.
     *
     * @param entries NewsEntries
     */
    private void computeFrequency(ArrayList<NewsEntry> entries) {
        ArrayList<String> tags = new ArrayList<>();
        for (NewsEntry entry : entries) {
            for (NewsEntry.NewsTag tag : entry.getTags()) {
                tags.add(tag.getValue());
            }
        }

        for (String tag : tags) {
            double n = 0;
            for (String s : tags) {
                if (s.equals(tag)) {
                    ++n;
                }
            }
            for (NewsEntry entry : entries) {
                for (NewsEntry.NewsTag entryTag : entry.getTags()) {
                    if (entryTag.getValue().equals(tag)) {
                        entryTag.setFrequency(n / tags.size());
                    }
                }
            }
        }
    }

    /**
     * Format any time.
     *
     * @param time The time you want to format.
     * @return Provided time as a string in yyMMdd format
     */
    private String formatTime(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
        return dtf.format(time);
    }

    /**
     * Format the current (system) time to yyMMdd format.
     *
     * @return Date-String in yyMMdd format
     */
    private String formatCurrentTime() {
        return formatTime(LocalDateTime.now());
    }

    /**
     * Make the news-endpoint url-string for any date.
     *
     * @param date The date in yyMMdd format
     * @return url of the news-endpoint (e.g. <a href="https://www.tagesschau.de/api2u/news?date=230910">news-endpoint</a>)
     */
    private String buildNewsURL(String date) {
        return TS_NEWS + "?date=" + date;
    }

    /**
     * Sort the given news by tag-frequency.
     *
     * @param entries NewsEntries
     */
    public void sortByTags(ArrayList<NewsEntry> entries) {
        entries.sort((a, b) -> Double.compare(b.getTagFrequency(), a.getTagFrequency()));
    }
}
