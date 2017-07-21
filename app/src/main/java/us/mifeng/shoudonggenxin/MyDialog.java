package us.mifeng.shoudonggenxin;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by shido on 2017/7/21.
 */

public class MyDialog  {
    public static void dialog (Activity activity,String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadAndInstall();
                    }
                })
                .setCancelable(false)
                .create();
        builder.show();
    }

    public static void downloadAndInstall() {

    }
}
