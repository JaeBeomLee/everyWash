package ga.washmose.mose.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by leejaebeom on 2016. 7. 31..
 */
public class UDP {
    public static int getDp(Context context, int px){
        DisplayMetrics matrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, px, matrics);
    }
}
