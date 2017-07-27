package us.mifeng.shoudonggenxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shido on 2017/7/25.
 */

public class SplashActivity extends Activity{

    private LinearLayout splash;
    private int count = 0;
    private int index = 3;
    private final int heheda = 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hand.sendEmptyMessage(heheda);
        initView();
        //initTimer();
    }

    private void initTimer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (index<=-1){
                    timer.cancel();
                    timer.purge();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));

                        }
                    });
                }
                index--;
            }
        },0,1000);
    }


    Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case heheda:
                    hand.sendEmptyMessageDelayed(heheda,1000);
                    index--;
                    if (index<-1){
                        hand.removeMessages(heheda);
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        finish();
                    }
                    break;
            }
        }
    };

    private void initView() {
        splash = (LinearLayout) findViewById(R.id.splash_ll);
        splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hand.removeMessages(heheda);
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        });
    }
}
