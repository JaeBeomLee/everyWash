package ga.washmose.mose;

import java.io.Serializable;

/**
 * Created by leejaebeom on 2016. 10. 3..
 */

public class ItemData implements Serializable {
    public String name, iconURL, progress, sellerID;
    public int minimum, price, priceCode, goodsCode, count = 0;

    public ItemData(String name, String iconURL, String sellerID,
                    int minimum, int price, int priceCode,
                    int goodsCode) {
        this.name = name;
        this.iconURL = iconURL;
        this.sellerID = sellerID;
        this.minimum = minimum;
        this.price = price;
        this.priceCode = priceCode;
        this.goodsCode = goodsCode;
    }

    public ItemData(String name, String iconURL,
                    int minimum, int price, int priceCode,
                    int goodsCode) {
        this.name = name;
        this.iconURL = iconURL;
        this.minimum = minimum;
        this.price = price;
        this.priceCode = priceCode;
        this.goodsCode = goodsCode;
    }
}
