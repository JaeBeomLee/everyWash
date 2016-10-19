package ga.washmose.mose.seller;

/**
 * Created by leejaebeom on 2016. 9. 27..
 */

public class SellerRequestItem {
    public String imageUrl;
    public String name;
    public String address;
    public String summary;
    public String request;
    public boolean author;

    public SellerRequestItem(String imageUrl, String name, String address, String request, String summary, boolean author) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.address = address;
        this.request = request;
        this.summary = summary;
        this.author = author;
    }
}
