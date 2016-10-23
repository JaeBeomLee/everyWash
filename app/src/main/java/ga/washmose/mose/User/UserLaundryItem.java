package ga.washmose.mose.User;

import java.io.Serializable;
import java.util.ArrayList;

import ga.washmose.mose.ItemData;

/**
 * Created by leejaebeom on 2016. 9. 27..
 */

public class UserLaundryItem implements Serializable{
    public String imageUrl;
    public String name;
    public String location;
    public String summary;
    public ArrayList<ItemData> items;
    public int latitude, longitude;

    public UserLaundryItem(String imageUrl, String name, String location, String summary, ArrayList<ItemData> items, int latitude, int longitude) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.location = location;
        this.summary = summary;
        this.items = items;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
