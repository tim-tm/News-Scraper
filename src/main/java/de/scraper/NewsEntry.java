package de.scraper;

import java.util.ArrayList;

/**
 * @author Tim
 */
@SuppressWarnings("unused")
public class NewsEntry {
    private String title;
    private String date;
    private String shareURL;
    private ArrayList<NewsTag> tags;

    public NewsEntry(String title, String date, String shareURL) {
        this.title = title;
        this.date = date;
        this.shareURL = shareURL;
        this.tags = new ArrayList<>();
    }

    public void addTag(NewsTag tag) {
        this.tags.add(tag);
    }

    public double getTagFrequency() {
        double d = 0;
        for (NewsTag tag : this.tags) {
            if (tag.getFrequency() > d) {
                d = tag.getFrequency();
            }
        }
        return d;
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

    public ArrayList<NewsTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<NewsTag> tags) {
        this.tags = tags;
    }

    public static final class NewsTag {
        private String value;
        private double frequency;

        public NewsTag(String value, double frequency) {
            this.value = value;
            this.frequency = frequency;
        }

        public NewsTag(String value) {
            this(value, 0);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public double getFrequency() {
            return frequency;
        }

        public void setFrequency(double frequency) {
            this.frequency = frequency;
        }
    }
}
