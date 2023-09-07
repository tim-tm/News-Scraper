package de.nscraper.api;

/**
 * @author Tim
 */
@SuppressWarnings("unused")
public class NewsEntry {
    private String title;
    private String date;
    private String shareURL;

    public NewsEntry(String title, String date, String shareURL) {
        this.title = title;
        this.date = date;
        this.shareURL = shareURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }
}
