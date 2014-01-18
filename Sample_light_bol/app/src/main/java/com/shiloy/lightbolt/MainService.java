package com.shiloy.lightbolt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.IBinder;

public class MainService extends Service {

    Camera cam;
    Context mctx = null;
    public static boolean event = true;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mctx = getApplicationContext();
        mctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (event) {
            /* TODO:
            * check with real device or with emulator
            * */
            cam = Camera.open();
            if (cam != null) {
                Parameters p = cam.getParameters();
                p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            event = !event;
            }
        } else {
            if (cam != null) {
                cam.stopPreview();
                cam.release();
                cam = null;
                event = !event;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
