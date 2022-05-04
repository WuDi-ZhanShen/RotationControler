package com.tile.rotation1;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

@TargetApi(Build.VERSION_CODES.N)
public class tileService extends TileService {

    @Override
    public void onStartListening() {
        SharedPreferences a = getSharedPreferences("Rotation_Preferences", 0);
        Tile tile = getQsTile();
        if (tile == null) {
            return;
        }
        tile.setLabel(SettingActivity.t(a.getInt("n", 0)));
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
        super.onStartListening();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile == null) {
            return;
        }
        SharedPreferences a = getSharedPreferences("Rotation_Preferences", 0);
        SharedPreferences.Editor b = getSharedPreferences("Global_State", 0).edit();
        SharedPreferences.Editor c = a.edit();
        if (a.getInt("n", 0) == 0) {
            c.putInt("n", 1);
            b.putInt("n", 1);
            tile.setLabel("锁定竖屏");
        } else {
            c.putInt("n", 0);
            b.putInt("n", 0);
            tile.setLabel("锁定横屏");
        }
        b.apply();
        c.apply();
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
        super.onClick();
    }

}
