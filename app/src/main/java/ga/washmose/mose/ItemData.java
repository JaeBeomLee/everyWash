package ga.washmose.mose;

import java.io.Serializable;

/**
 * Created by leejaebeom on 2016. 10. 3..
 */

public class ItemData implements Serializable {
    public String name, iconURL, progress;
    public int count, price;

    public ItemData(String iconURL, String name, int count, int price) {
        this.iconURL = iconURL;
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public ItemData(String iconURL, String name, int count, int price, String progress) {
        this.name = name;
        this.iconURL = iconURL;
        this.progress = progress;
        this.count = count;
        this.price = price;
    }
}
