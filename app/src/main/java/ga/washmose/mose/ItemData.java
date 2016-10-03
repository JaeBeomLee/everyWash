package ga.washmose.mose;

/**
 * Created by leejaebeom on 2016. 10. 3..
 */

public class ItemData {
    public String name, iconURL;
    public int count, price;

    public ItemData(String iconURL, String name, int count, int price) {
        this.iconURL = iconURL;
        this.name = name;
        this.count = count;
        this.price = price;
    }
}
