package ga.washmose.mose.Util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import ga.washmose.mose.pre.LoginActivity;

/**
 * Created by leejaebeom on 2016. 10. 18..
 */

public class UDialog {
    public static void setDialog(Context context, String msg){
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("확인", null)
                .show();

    }
}
