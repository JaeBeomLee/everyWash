package ga.washmose.mose.User;

import java.io.Serializable;
import java.util.ArrayList;

import ga.washmose.mose.ItemData;

/**
 * Created by leejaebeom on 2016. 9. 27..
 */

public class UserLaundryItem implements Serializable{
    public int id;
    public String imageUrl;
    public String name;
    public String location;
    public String summary;
    public ArrayList<ItemData> items;
    public double latitude, longitude;

    public UserLaundryItem(int id, String imageUrl, String name, String location, String summary, ArrayList<ItemData> items, double latitude, double longitude) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.location = location;
        this.summary = summary;
        this.items = items;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
