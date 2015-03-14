package de.zb42.roemke.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by roemke on 08.03.15.
 */
public class KRService extends Service {

    private KRNanoServer nanoServer;


    @Override
    public IBinder onBind(Intent intent) {
        //TODO
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        nanoServer = new KRNanoServer(this);
        try {
            Toast.makeText(getApplicationContext(), "Service JSON-Info wurde gestartet", Toast.LENGTH_LONG).show();
            nanoServer.start(); //maybe better in try catch , if port is blocked or something like that, see serverrunner
        } catch (IOException ex)
        {
            Toast.makeText(getApplicationContext(), "Server Start nicht möglich " + ex.toString(), Toast.LENGTH_LONG).show();
        }
        return START_STICKY; //want to keep service running
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Service JSON-Info wurde beendet", Toast.LENGTH_LONG).show();
        nanoServer.stop();
    }

}
