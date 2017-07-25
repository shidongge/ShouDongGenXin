package us.mifeng.shoudonggenxin;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import us.mifeng.shoudonggenxin.progress.ProgressImageView;

/**
 * Created by shido on 2017/7/24.
 */

public class VPAdapter extends PagerAdapter {
    private List<ProgressImageView> list;
    private Context context;
    public VPAdapter(List<ProgressImageView> list, Context context){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}
