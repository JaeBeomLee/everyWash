package ga.washmose.mose.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

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
    public static void setDialog(Context context, String msg, DialogInterface.OnClickListener positive){
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("확인", positive)
                .show();

    }
}
