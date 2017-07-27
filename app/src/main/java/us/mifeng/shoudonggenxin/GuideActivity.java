package us.mifeng.shoudonggenxin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import us.mifeng.shoudonggenxin.progress.ProgressImageView;
import us.mifeng.shoudonggenxin.progress.ProgressModelLoader;

import static us.mifeng.shoudonggenxin.R.drawable.yuan2;

/**
 * Created by shido on 2017/7/24.
 */

public class GuideActivity extends Activity implements View.OnClickListener {
    private String imgurl = HttpInter.home;
    private String string;
    private List<String> str_list ;
    private List<ProgressImageView> img_list = new ArrayList<>();
    private ViewPager mvp;
    private static final String TAG = "GuideActivity";
    private LinearLayout ll;
    private VPAdapter adapter;
    private Button mBtn;
    private SharedUtils sharedUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        sharedUtils = new SharedUtils();

        String first = sharedUtils.getSp(GuideActivity.this, "first");
        if (!TextUtils.isEmpty(first)){
            startActivity(new Intent(GuideActivity.this,SplashActivity.class));
            finish();
        }else {
            initDeta();
        }
        sharedUtils.saveSp(GuideActivity.this,"first","hehe");
        initView();

    }

    private void initView() {
        mvp = (ViewPager) findViewById(R.id.mVp);
        ll = (LinearLayout) findViewById(R.id.ll);
        mBtn = (Button) findViewById(R.id.mBtn);
        mBtn.setVisibility(View.GONE);
        mBtn.setOnClickListener(this);
        mvp.setOffscreenPageLimit(0);
    }

    private void initDeta() {
        OkHttpClient ok = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(imgurl)
                .build();
        Call call = ok.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()){
                    string = response.body().string();
                    Message mess = hand.obtainMessage();
                    mess.obj= string;
                    mess.what=200;
                    hand.sendMessage(mess);
                }
            }
        });
    }
    Handler hand  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==200){
                string = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    String status = jsonObject.getString("status");
                    if ("200".equals(status)){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray guidepic = data.getJSONArray("guidepic");
                        str_list = new ArrayList<>();
                        for (int i = 0;i<guidepic.length();i++){
                            ProgressImageView piv = (ProgressImageView) LayoutInflater.from(GuideActivity.this).
                                    inflate(R.layout.progressimageview,null,false);
                            ImageView imageView = piv.getImageView();
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            String imgurl2  = (String) guidepic.get(i);
                            Glide.with(GuideActivity.this)
                                    .using(new ProgressModelLoader(new ProgressHandler(GuideActivity.this,piv)))
                                    .load(imgurl2)
                                    .placeholder(R.drawable.loading)
                                    .into(imageView);
                            img_list.add(piv);
                            ImageView imageView1 = new ImageView(GuideActivity.this);
                            imageView1.setImageResource(R.drawable.yuan);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(50, 50);
                            lp.setMargins(10,0,10,0);
                            imageView1.setLayoutParams(lp);
                            ll.addView(imageView1);
                        }
                        //给第一次进来的小圆点添加颜色
                        ImageView firstIv = (ImageView) ll.getChildAt(0);
                        firstIv.setImageResource(yuan2);
                        adapter = new VPAdapter(img_list,GuideActivity.this);
                        mvp.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initPager();
            }
        }
    };

    private void initPager() {
        mvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                yuanDian();
                ImageView im = (ImageView) ll.getChildAt(position);
                im.setImageResource(yuan2);
                if(position==(adapter.getCount()-1)){
                    mBtn.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(GuideActivity.this, R.anim.anniu);
                    animation.setInterpolator(new OvershootInterpolator());
                    mBtn.setAnimation(animation);
                }else {
                    mBtn.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void yuanDian() {
        for (int i = 0 ;i<ll.getChildCount();i++){
            ((ImageView) ll.getChildAt(i)).setImageResource(R.drawable.yuan);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn:

                startActivity(new Intent(GuideActivity.this,SplashActivity.class));
                finish();
                break;
        }
    }

    private static class ProgressHandler extends Handler {
        private final WeakReference<Activity> mActivity;
        private final ProgressImageView mProgressImageView;

        public ProgressHandler(Activity activity, ProgressImageView progressImageView) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
            mProgressImageView = progressImageView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        int percent = msg.arg1 * 100 / msg.arg2;
                        mProgressImageView.setProgress(percent);
                        if (percent>=100){
                            mProgressImageView.hideTextView();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }


}
