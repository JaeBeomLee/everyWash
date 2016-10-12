package ga.washmose.mose;

/**
 * Created by leejaebeom on 2016. 9. 27..
 */

public class UserLaundryItem {
    public String imageUrl;
    public String name;
    public String location;
    public String summary;

    public UserLaundryItem(String imageUrl, String name, String location, String summary) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.location = location;
        this.summary = summary;
    }
}
