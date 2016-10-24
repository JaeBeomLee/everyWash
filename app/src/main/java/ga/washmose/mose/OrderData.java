package ga.washmose.mose;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by leejaebeom on 2016. 10. 16..
 */

public class OrderData implements Serializable, Parcelable {
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

    public OrderData(String profileImageUrl, String name, String summary, Calendar request, boolean author, int code, int progress, String phone, Calendar collectionDate, Calendar completeDate, String address, ArrayList<ItemData> items) {
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

    protected OrderData(Parcel in) {
        headerImageUrl = in.readString();
        profileImageUrl = in.readString();
        name = in.readString();
        title = in.readString();
        summary = in.readString();
        author = in.readByte() != 0;
        code = in.readInt();
        progress = in.readInt();
        phone = in.readString();
        address = in.readString();
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headerImageUrl);
        dest.writeString(profileImageUrl);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeByte((byte) (author ? 1 : 0));
        dest.writeInt(code);
        dest.writeInt(progress);
        dest.writeString(phone);
        dest.writeString(address);
    }
}
