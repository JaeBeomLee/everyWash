package ga.washmose.mose.Util;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by C on 15. 8. 6..
 */
public class UDate {
    private final static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd (E)", Locale.KOREA);
    private final static DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
    private final static DateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
    private final static DateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
    private final static DateFormat periodFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    private final static DateFormat logFormat = new SimpleDateFormat("yy/MM/dd HH:mm", Locale.KOREA);

    public static Date getDate(String date) throws ParseException {
        return dateTimeFormat.parse(date);
    }

    public static String getDateTimeFormat(Date date) {
        return dateTimeFormat.format(date);
    }

    public static String getDateFormat(Date date) {
        return dateFormat.format(date);
    }

    public static String getTimeFormat(Date date) {
        return timeFormat.format(date);
    }

    public static String getSimpleDateFormat(Date date) {
        return simpleDateFormat.format(date);
    }

    public static String getLogFormat(Date date) {
        return logFormat.format(date);
    }

    public static String getTimeStampFormat(Date date) {
        return timeStampFormat.format(date);
    }

    public static String getPeriodFormat(Date date) {
        return periodFormat.format(date);
    }

//    public static Calendar getServerDate(Context context) throws ParseException, JSONException {
//        JSONObject req = new JSONObject();
//        String urlString = MainActivity.getServerIp(context) + "/time";
//        String response = CHttps.getResponse(context, urlString, req);
//        if (response != null) {
//            JSONObject resObj = new JSONObject(response);
//            String nowDate = resObj.optString("date");
//            if (nowDate != null) {
//                Calendar today = Calendar.getInstance();
//                today.setTime(UDate.getDate(nowDate));
//                return today;
//            }
//        }
//        return null;
//    }
}
