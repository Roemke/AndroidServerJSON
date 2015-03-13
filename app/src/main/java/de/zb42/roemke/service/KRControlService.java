package de.zb42.roemke.service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class KRControlService extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_service);
        if (isMyServiceRunning(KRService.class)) {
            Button b = (Button) findViewById(R.id.bStart);
            b.setEnabled(false);
            b = (Button) findViewById(R.id.bStop);
            b.setEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startService(View view) {
        Button b = (Button) findViewById(R.id.bStart);
        b.setEnabled(false);
        b = (Button) findViewById(R.id.bStop);
        b.setEnabled(true);
        Intent i = new Intent(KRControlService.this, KRService.class);

        startService(i);
    }

    public void stopService(View view) {
        Button b = (Button) findViewById(R.id.bStart);
        b.setEnabled(true);
        b = (Button) findViewById(R.id.bStop);
        b.setEnabled(false);
        Intent i = new Intent(KRControlService.this, KRService.class);
        stopService(i);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
