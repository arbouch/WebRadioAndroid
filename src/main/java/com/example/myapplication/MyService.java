package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private Button btn;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        createNotificationChannel();


        new Player().execute("http://icecast.funradio.fr/fun-1-44-128");

        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(NOTIF_CHANNEL_ID, "Example Service Channel",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);

        }
    }

    private void startForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running background")
                .setContentIntent(pendingIntent)
                .build());

    }

    class Player extends AsyncTask<String, Void, Boolean> {

        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(String... strings) {
            Thread th=new Thread(){




                @Override
                public void run() {
              //      String audiouri="http://icecast.funradio.fr/fun-1-44-128";
             //       MediaPlayer.create(getApplicationContext(), Uri.parse(audiouri)).start();
               //             new Player().execute("http://icecast.funradio.fr/fun-1-44-128");

                }
            };
           th.start();



            Boolean prepared = false;

            try {
//                mediaPlayer.setDataSource(strings[0]);

               String audiouri="http://streaming.radio.rtl.fr/rtl-1-48-192";
                MediaPlayer.create(getApplicationContext(), Uri.parse(audiouri)).start();
/*
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        btn.setText("Launch Streaming");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
*/
                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }
    }
}