package de.nscraper.api;

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
 * @author Tim
 */
@SuppressWarnings("unused")
public class TagesschauAPI {
    private static final String TS_ENDPOINT = "https://www.tagesschau.de/api2u";
    private static final String TS_NEWS = TS_ENDPOINT + "/news";

    private final HttpClient client;

    public TagesschauAPI() {
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Fetch the news using the Tagesschau APIv2
     *
     * @param date Date in yyMMdd format.
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

        ArrayList<NewsEntry> entries = new ArrayList<>();
        JSONObject object = new JSONObject(res);
        JSONArray news = object.getJSONArray("news");
        for (int i = 0; i < news.length(); i++) {
            JSONObject obj = news.getJSONObject(i);
            if (obj.has("type") && obj.getString("type").equals("video")) continue;

            String title = obj.getString("title");
            if (onlyBreaking && !title.contains("+")) continue;

            entries.add(new NewsEntry(title, obj.getString("date"), obj.getString("shareURL")));
        }
        return entries;
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
     * @param days Days to go back.
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

    private String formatTime(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");
        return dtf.format(time);
    }

    public String formatCurrentTime() {
        return formatTime(LocalDateTime.now());
    }

    private String buildNewsURL(String date) {
        return TS_NEWS + "?date=" + date;
    }
}
