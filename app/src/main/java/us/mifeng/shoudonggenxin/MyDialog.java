package us.mifeng.shoudonggenxin;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shido on 2017/7/21.
 */

public class MyDialog  {
    public static void dialog (final Activity activity, String title, String msg,final String url){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadAndInstall(activity,url);
                    }
                })
                .setCancelable(false)
                .create();
        builder.show();
    }

    public static void downloadAndInstall(final Activity act, String url) {
        OkHttpClient ok = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = ok.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    byte[] bytes = response.body().bytes();
                    if (bytes!=null&&bytes.length>0){
                        File file = new File(Environment.getExternalStorageDirectory() + "/how");
                        if (!file.exists()){
                            file.mkdirs();
                        }
                        File file1 = new File(file, "app.apk");
                        if (!file1.exists()){
                            file1.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(file1);
                        fos.write(bytes);
                        fos.close();
                        bytes=null;
                        //安装逻辑
                        InstallApk(act,file1);
                    }
                }
            }
        });
    }

    public static void InstallApk(Activity act,File file){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+file.getAbsolutePath()),
                "application/vnd.android.package-archive");
        //startActivityForResult在sdk中有明确说明，
        // 如果intent类型是ACTION_VIEW或者ACTION_MAIN那是不会得到返回结果的
        act.startActivity(intent);
        //疑问？如何监听apk是否安装成功
    }
}
