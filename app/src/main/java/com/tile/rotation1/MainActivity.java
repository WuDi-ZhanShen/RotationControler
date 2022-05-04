package com.tile.rotation1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioButton;

public class MainActivity extends Activity {

    public void a(View view) {
        int k = Integer.parseInt(view.getTag().toString());
        if (RotationService.f) {
            getSharedPreferences("Rotation_Preferences", 0).edit().putInt("n", k).apply();
            getSharedPreferences("Global_State", 0).edit().putInt("n", k).apply();
        }else b();
    }

    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,Uri.parse("package:" + getPackageName())));
            if (!Settings.System.canWrite(this))
                startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,Uri.parse("package:" + getPackageName())));
        }
        if (!RotationService.f) b();
        super.onCreate(bundle);
        if (((UiModeManager) getSystemService(Service.UI_MODE_SERVICE)).getNightMode()==UiModeManager.MODE_NIGHT_NO)
            setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);
        setContentView(R.layout.b);
        getWindow().getAttributes().alpha=0.85f;
        switch (getSharedPreferences("Rotation_Preferences", 0).getInt("n", 99)) {
            case 0:
                ((RadioButton) findViewById(R.id.r0)).setChecked(true);
                break;
            case 1:
                ((RadioButton) findViewById(R.id.r1)).setChecked(true);
                break;
            case 8:
                ((RadioButton) findViewById(R.id.r8)).setChecked(true);
                break;
            case 9:
                ((RadioButton) findViewById(R.id.r9)).setChecked(true);
                break;
            case 10:
                ((RadioButton) findViewById(R.id.r10)).setChecked(true);
                break;
            case 13:
                ((RadioButton) findViewById(R.id.r13)).setChecked(true);
                break;
            case 99:
                ((RadioButton) findViewById(R.id.r99)).setChecked(true);
                break;

        }
    }

    public void b() {
        Bundle bundle = new Bundle();
        bundle.putString(":settings:fragment_args_key", new ComponentName(getPackageName(), RotationService.class.getName()).flattenToString());
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).putExtra(":settings:fragment_args_key", new ComponentName(getPackageName(), RotationService.class.getName()).flattenToString()).putExtra(":settings:show_fragment_args", bundle));
    }
    public void c(View v) {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }
}