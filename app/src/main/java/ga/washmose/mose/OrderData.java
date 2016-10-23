package ga.washmose.mose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class OrderData implements Serializable {
    public String headerImageUrl;
    public String profileImageUrl;
    public String name;
    public String title;
    public String summary;
    public Calendar request;
    public boolean author;
    public int code;
    public int progress;
    public String phone;
    public Calendar collectionDate;
    public Calendar completeDate;
    public String address;
    public ArrayList<ItemData> items;

    public OrderData(String headerImageUrl, String profileImageUrl, String name, String title, String summary, Calendar request, boolean author, int code, int progress, String phone, Calendar collectionDate, Calendar completeDate, String address, ArrayList<ItemData> items) {
        this.headerImageUrl = headerImageUrl;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.title = title;
        this.summary = summary;
        this.request = request;
        this.author = author;
        this.code = code;
        this.progress = progress;
        this.phone = phone;
        this.collectionDate = collectionDate;
        this.completeDate = completeDate;
        this.address = address;
        this.items = items;
    }
}
