package ga.washmose.mose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class OrderData implements Serializable {
    public int code;
    public int progress;
    public String phone;
    public Calendar collectionDate;
    public Calendar completeDate;
    public String address;
    public ArrayList<ItemData> items;
}
