package ga.washmose.mose.main;

/**
 * Created by leejaebeom on 2016. 9. 27..
 */

public class MainItem {
    String imageUrl;
    String name, location, summary;

    public MainItem(String imageUrl, String name, String location, String summary) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.location = location;
        this.summary = summary;
    }
}
