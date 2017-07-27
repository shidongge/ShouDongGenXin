package us.mifeng.shoudonggenxin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shido on 2017/7/26.
 */

public class SharedUtils {
    private String first = "first";
    public void saveSp(Context context,String ksy,String volue){
        SharedPreferences sp = context.getSharedPreferences(first, 0);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(ksy,volue);
        edit.commit();
    }
    public String getSp(Context context,String key){
        String str = null;
        SharedPreferences sp = context.getSharedPreferences(first, 0);
        str = sp.getString(key,"");
        return str;
    }
}
