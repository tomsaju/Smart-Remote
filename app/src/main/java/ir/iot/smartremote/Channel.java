package ir.iot.smartremote;

/**
 * Created by Dreamz on 27-08-2018.
 */

public class Channel {
    int index;
    String title;
    String imageUrl;
    String category;
    String language;


    public Channel(int index, String title, String imageUrl) {
        this.index = index;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public Channel() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
