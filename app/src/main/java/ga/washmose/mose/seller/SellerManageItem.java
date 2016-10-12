package ga.washmose.mose.seller;

/**
 * Created by leejaebeom on 2016. 9. 27..
 */

public class SellerManageItem {
    public String date;
    public String remainDate;
    public String address;
    public int code;
    public String phone;

    public SellerManageItem(String date, String remainDate, int code, String address) {
        this.date = date;
        this.remainDate = remainDate;
        this.code = code;
        this.address = address;
    }

    public SellerManageItem(String date, int code, String address, String phone) {
        this.date = date;
        this.code = code;
        this.address = address;
        this.phone = phone;
    }
}
