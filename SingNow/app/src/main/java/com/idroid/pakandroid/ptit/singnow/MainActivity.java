package com.idroid.pakandroid.ptit.singnow;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Environment;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.IOException;

public class MainActivity extends YouTubeBaseActivity {
    Button play, start, stop;
    YouTubePlayerView mYoutubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        play = findViewById(R.id.btn_play_record);
        stop = findViewById(R.id.btn_stop_record);
        start = findViewById(R.id.btn_start_record);
        mYoutubePlayerView = (YouTubePlayerView) findViewById(R.id.video_karaoke);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // link video o day
                youTubePlayer.loadVideo("gNr4_kPjMEY");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myAudioRecorder=new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                    mYoutubePlayerView.initialize(YoutubeConfig.getApiKey(),mOnInitializedListener);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                stop.setEnabled(true);
                start.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });
       stop.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try{
                   myAudioRecorder.stop();
                   myAudioRecorder.release();
                   myAudioRecorder = null;


               }catch (Exception e){
                   e.printStackTrace();
               }
               stop.setEnabled(false);
               play.setEnabled(true);
               Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
           }
       });
       play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MediaPlayer m = new MediaPlayer();
               try {
                   m.setDataSource(outputFile);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               try {
                   m.prepare();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               m.start();
               Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
               m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                   @Override
                   public void onCompletion(MediaPlayer mediaPlayer) {
                       Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                       start.setEnabled(true);
                       stop.setEnabled(false);
                       play.setEnabled(false);
                   }
               });
           }
       });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                permissionToWriteAccepted  = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) MainActivity.super.finish();
        if (!permissionToWriteAccepted ) MainActivity.super.finish();
    }

}
