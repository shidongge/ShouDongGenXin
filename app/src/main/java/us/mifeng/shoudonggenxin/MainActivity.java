package us.mifeng.shoudonggenxin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import us.mifeng.shoudonggenxin.fragment.ChatFragment;
import us.mifeng.shoudonggenxin.fragment.HomeFragment;
import us.mifeng.shoudonggenxin.fragment.MineFragment;

public class MainActivity extends FragmentActivity {
    private String path = "https://guaju.github.io/versioninfo.json";
    private String path2 = "https://shidongge.github.io/versioninfo.json";
    private String string;
    private static final String TAG = "MainActivity";
    private FragmentTabHost host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckUpdate();
        initView();
    }

    private void initView() {
        host = (FragmentTabHost) findViewById(android.R.id.tabhost);
        host.setup(MainActivity.this,getSupportFragmentManager(),android.R.id.tabcontent);
        TabHost.TabSpec home = host.newTabSpec("home").setIndicator("首页");
        TabHost.TabSpec liaotian = host.newTabSpec("liaotian").setIndicator("聊天");
        TabHost.TabSpec mine = host.newTabSpec("mine").setIndicator("我的");
        host.addTab(home,HomeFragment.class,null);
        host.addTab(liaotian, ChatFragment.class,null);
        host.addTab(mine, MineFragment.class,null);
    }

    private void CheckUpdate() {
        //创建OKhttpClient对象
        OkHttpClient ok = new OkHttpClient();
        //创建builder对象
        Request.Builder builder = new Request.Builder();
        final Request request = builder.get()
                .url(path2)
                .build();
        Call call = ok.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(Response response) throws IOException {
                string = response.body().string();
                Message mess = new Message();
                mess.what=200;
                mess.obj= string;
                han.sendMessage(mess);
            }
        });
    }
    Handler han = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==200){
                string = (String) msg.obj;
                try {
                    JSONObject jo = new JSONObject(string);
                    String status = jo.getString("status");
                    if ("200".equals(status)){
                        JSONObject data = jo.getJSONObject("data");
                        String version = data.getString("version");
                        Log.e(TAG, "wangluo: "+version );
                        String info = data.getString("info");
                        String appurl = data.getString("appurl");
                        Log.e(TAG, "網址: "+appurl);
                        PackageManager manager = getPackageManager();
                        PackageInfo myinfo = manager.getPackageInfo(getPackageName(), 0);
                        String myversion = myinfo.versionName;
                        Log.e(TAG, "bendi: "+myversion );
                        if (!version.equals(myversion)){
                            MyDialog.dialog(MainActivity.this,"重大更新",info,appurl);
                        }else {
                            Toast.makeText(MainActivity.this, "已是最新版本，无需更新", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
