package ga.washmose.mose;

import java.io.Serializable;

/**
 * Created by leejaebeom on 2016. 10. 3..
 */

public class ItemData implements Serializable {
    public String name, iconURL, progress, sellerID;
    public int count, price, priceCode, goodsCode;

    public ItemData(String name, String iconURL, String sellerID,
                    int count, int price, int priceCode,
                    int goodsCode) {
        this.name = name;
        this.iconURL = iconURL;
        this.sellerID = sellerID;
        this.count = count;
        this.price = price;
        this.priceCode = priceCode;
        this.goodsCode = goodsCode;
    }

    public ItemData(String name, String iconURL,
                    int count, int price, int priceCode,
                    int goodsCode) {
        this.name = name;
        this.iconURL = iconURL;
        this.count = count;
        this.price = price;
        this.priceCode = priceCode;
        this.goodsCode = goodsCode;
    }
}
