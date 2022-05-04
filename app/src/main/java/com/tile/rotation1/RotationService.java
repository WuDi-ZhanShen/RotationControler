package com.tile.rotation1;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityWindowInfo;

public class RotationService extends AccessibilityService implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static boolean f=false;
    private WindowManager a;
    private View c;
    private WindowManager.LayoutParams e;
    private SharedPreferences b,g;
    private boolean d = false;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        a = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        e = new WindowManager.LayoutParams();
        e.height = 0;
        e.width = 0;
        e.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        if (Build.VERSION.SDK_INT >= 22)
            e.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        else
            e.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        c = new View(this);
        b = getSharedPreferences("Rotation_Preferences", 0);
        g = getSharedPreferences("Global_State", 0);
        b.registerOnSharedPreferenceChangeListener(this);
        c(b.getInt("n",99));
        f=true;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent ae) {
        if ( !ae.isFullScreen() || ae.getPackageName()==null ||(ae.getClassName()!=null && ae.getClassName().toString().equals("android.inputmethodservice.SoftInputWindow"))) return;
        if (Build.VERSION.SDK_INT >= 24)
            for (AccessibilityWindowInfo g : getWindows()) {
                if (g.getType() == AccessibilityWindowInfo.TYPE_SPLIT_SCREEN_DIVIDER) return;
                if (Build.VERSION.SDK_INT >= 26 && g.isInPictureInPictureMode()) return;
            }
        String q = null;
        if (Build.VERSION.SDK_INT >= 26) {
            UsageEvents u = ((UsageStatsManager) getSystemService(Service.USAGE_STATS_SERVICE)).queryEvents(System.currentTimeMillis() - 20000, System.currentTimeMillis());
            UsageEvents.Event s = new UsageEvents.Event();
            while (u.hasNextEvent()) u.getNextEvent(s);
            //if (s.getEventType()==23&& !s.getPackageName().equals(p)){
            //    q = p;
            //}
            if (s.getEventType()==1) q = s.getPackageName();
            Log.d("TAG", ": "+q);
            if (q==null || q.equals("android")) return;
        } else
            q = ae.getPackageName().toString();
        int m=g.getInt(q, 98);
        if (m==97) return;
        if (m != 98)
            b.edit().putInt("n", m).apply();
        else
            b.edit().putInt("n", g.getInt("n", 99)).apply();
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        b.unregisterOnSharedPreferenceChangeListener(this);
        if (d) {
            a.removeView(c);
        }
        d = false;
        f=false;
    }
    
    public void c(int i){
        if (i == 0 || i == 1 || i == 8 || i == 9) {
            try{
                Settings.System.putInt(getContentResolver(), "user_rotation", i<=1?1-i:11-i);
            } catch (SecurityException ignored){
            }
        }
        if (i == 99) {
            if (d) {
                a.removeView(c);
            }
            d = false;
        } else {
            e.screenOrientation = i;
            if (!d) {
                a.addView(c, e);
                d = true;
                return;
            }
            a.updateViewLayout(c, e);
        }
    }

    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        c(sharedPreferences.getInt("n", 99));
    }

}

