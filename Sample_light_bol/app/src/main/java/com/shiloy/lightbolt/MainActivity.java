package com.shiloy.lightbolt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int density = getResources().getDisplayMetrics().densityDpi;
        super.onCreate(savedInstanceState);
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(this, "LDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(this, "MDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(this, "HDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(this, "XHDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Toast.makeText(this, "XXHDPI", Toast.LENGTH_SHORT).show();
                break;
            case DisplayMetrics.DENSITY_TV:
                Toast.makeText(this, "TV", Toast.LENGTH_SHORT).show();
                break;
        }

        startService(new Intent(this, MainService.class));
        super.onCreate(savedInstanceState);
        finish();
    }
}
