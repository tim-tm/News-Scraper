package de.scraper;

import java.util.ArrayList;

/**
 * An object to store the responses from various news API's.
 *
 * @author Tim
 * @since 0.0.1
 */
public class NewsEntry {
    private String title;
    private String date;
    private String shareURL;
    private ArrayList<NewsTag> tags;

    /**
     * This will initialize a new NewsEntry.
     *
     * @param title The title of an article
     * @param date When was the article released?
     * @param shareURL The url to access the article
     */
    public NewsEntry(String title, String date, String shareURL) {
        this.title = title;
        this.date = date;
        this.shareURL = shareURL;
        this.tags = new ArrayList<>();
    }

    /**
     * Add NewsTags to an NewsEntry.
     *
     * @param tag The NewsTag that will be added.
     */
    public void addTag(NewsTag tag) {
        this.tags.add(tag);
    }

    /**
     * Find the biggest frequency of every tag store in a NewsEntry.
     *
     * @return The biggest tag-frequency in the collection of NewsTags.
     */
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

    /**
     * Each NewsEntry stores a collection of NewsTags in order to sort them by the tag that occours the most.
     *
     * @author Tim
     * @since 0.0.1
     */
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
