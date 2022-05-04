package com.tile.rotation1;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ListActivity;
import android.app.Service;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends ListActivity {

    ListView a;
    SharedPreferences b;
    int c=0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.d,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.s:
                on("",0);
                break;
            case R.id.u:
                on("",1);
                break;
            case R.id.h:
                on("",2);
                break;
        }
        return true;
    }

    @Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (((UiModeManager) getSystemService(Service.UI_MODE_SERVICE)).getNightMode()==UiModeManager.MODE_NIGHT_NO)
            setTheme(android.R.style.Theme_DeviceDefault_Light);
        if (Build.VERSION.SDK_INT >= 26)
            if (((AppOpsManager) getSystemService(Service.APP_OPS_SERVICE)).checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(),getPackageName()) != AppOpsManager.MODE_ALLOWED)
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        setContentView(R.layout.c);
        b = getSharedPreferences("Global_State", 0);
        a = getListView();
        //TranslateAnimation animation = new TranslateAnimation(-50f, 0f, -30f, 0f);
        //animation.setDuration(200);
        //LayoutAnimationController controller = new LayoutAnimationController(animation, 0.1f);
        //controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        //a.setLayoutAnimation(controller);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        on("",1);
                    }
                });
            }
        }).start();

    }

    public void o(View view) {
        on(((EditText) findViewById(R.id.T)).getText().toString(),c);
    }
    public void on(String s,int i) {
        c=i;
        List<PackageInfo> list2=getPackageManager().getInstalledPackages(0);
        ArrayList list = new ArrayList();
            for (PackageInfo o : list2) {
                if (i==1 &&(o.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM)
                    continue;
                String p=o.packageName;
                int m = b.getInt(p,98);
                if (i==2 && m==98)
                    continue;
                String l=o.applicationInfo.loadLabel(getPackageManager()).toString();
                if (!l.contains(s)&&!p.contains(s)) continue;
                    Map map = new HashMap();
                    map.put("l", l);
                    map.put("i", o.applicationInfo.loadIcon(getPackageManager()));
                    map.put("p", p);
                    map.put("s", t(m));
                    list.add(map);

            }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.r, new String[] { "p","l","i","s" }, new int[] { R.id.a,R.id.b,R.id.c,R.id.d});
        adapter.setViewBinder(new ViewBinder() {
            public boolean setViewValue(View v, Object d, String t) {
                if(v instanceof ImageView  && d instanceof Drawable){
                    ((ImageView) v).setImageDrawable((Drawable) d);
                    return true;
                }else
                    return false;
            }
        });
        a.setAdapter(adapter);
        a.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p=((TextView) view.findViewById(R.id.a)).getText().toString();
                SharedPreferences.Editor e = b.edit();
                TextView t=view.findViewById(R.id.d);
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(SettingActivity.this);
                inputDialog.setTitle("当打开\""+((TextView) view.findViewById(R.id.b)).getText().toString()+"\"时：")
                        .setNegativeButton("返回", null)
                        .setNeutralButton("清除设定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                e.putInt(p, 98).apply();
                                t.setText(null);
                            }
                        })
                        .setSingleChoiceItems(new String[]{"保持当前方向","锁定横屏", "锁定竖屏","锁定逆向横屏", "锁定逆向竖屏", "四向任意旋转","关闭方向控制"},-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingActivity.this, "设定成功", Toast.LENGTH_SHORT).show();
                                switch(which) {
                                    case 0:
                                        e.putInt(p, 97);
                                        break;
                                    case 1:
                                    case 2:
                                        e.putInt(p, which-1);
                                        break;
                                    case 3:
                                    case 4:
                                    case 5:
                                        e.putInt(p, which+5);
                                        break;
                                    case 6:
                                        e.putInt(p, 99);
                                        break;
                                }
                                e.apply();
                                t.setText(t(b.getInt(p,98)));
                            }
                        })
                        .show();
            }
        });
    }

    public static String t(int i){
        switch (i){
            case 97:
                return "保持当前";
            case 0:
                return "锁定横屏";
            case 1:
                return "锁定竖屏";
            case 8:
                return "逆向横屏";
            case 9:
                return "逆向竖屏";
            case 10:
                return "四向旋转";
            case 13:
                return "系统优先";
            case 99:
                return "关闭控制";
            default:
                return "";
        }
    }

}