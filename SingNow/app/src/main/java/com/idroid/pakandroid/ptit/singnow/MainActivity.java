package com.idroid.pakandroid.ptit.singnow;

import android.content.pm.PackageInfo;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.facebook.FacebookSdk;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;



public class MainActivity extends YouTubeBaseActivity implements IGiaoTiep {
    Button play, start, stop, share;
    ImageButton btnPlay, btnStop, btnReplay, btnSave;
    TextView txtTimeSong, txtTimeTotal, txtSongTitle;
    VideoView karaokeView;
    SeekBar seekBarSong;
    private MediaRecorder myAudioRecorder;
    private MediaPlayer mediaPlayer;
    private String outputFile = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference httpsReference = storage.getReference();
    private boolean permissionToRecordAccepted = false;
    private boolean permissionToWriteAccepted = false;
    private String [] permissions = {"android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        play = findViewById(R.id.btn_play_record);
        stop = findViewById(R.id.btn_stop_record);
        start = findViewById(R.id.btn_start_record);
        share = findViewById(R.id.share);
        karaokeView = findViewById(R.id.video_karaoke);
        mapping();
        stop.setEnabled(false);
        play.setEnabled(false);
        Intent intent = getIntent();
        final String title = intent.getStringExtra("TITLE");
        downloadVideo(title);
        outputFile = getSDCardDirectory() + "recording123.3gp";
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                karaokeView.start();
                stop.setEnabled(true);
                start.setEnabled(false);
                karaokeView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stop.setEnabled(false);
                        play.setEnabled(true);
                        myAudioRecorder.stop();
                        myAudioRecorder.release();
                        myAudioRecorder = null;
                        karaokeView.stopPlayback();
                    }
                });
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
                karaokeView.stopPlayback();
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play.setEnabled(false);
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                setTimeTotal();
                updateTimeSong();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
                play.setEnabled(false);
                btnPlay.setEnabled(true);
                btnStop.setEnabled(true);
                btnReplay.setEnabled(true);
                btnSave.setEnabled(true);
                btnPlay.setImageResource(R.drawable.icon_pause_button);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
                        start.setEnabled(true);
                        stop.setEnabled(false);
                    }
                });
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                ShareDialog shareDialog = new ShareDialog(MainActivity.this);
                shareDialog.show(content);
            }
        });
        try {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(
                        "com.idroid.pakandroid.ptit.singnow",
                        PackageManager.GET_SIGNATURES);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NoSuchAlgorithmException e) {

        }
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.icon_play_button);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.icon_pause_button);
                }
                setTimeTotal();
                updateTimeSong();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                btnPlay.setImageResource(R.drawable.icon_play_button);
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                btnPlay.setEnabled(false);
                btnStop.setEnabled(false);
                btnReplay.setEnabled(false);
                btnSave.setEnabled(false);
                try {
                    mediaPlayer = new MediaPlayer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                start.setEnabled(true);
                downloadVideo(title);
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

    public String getSDCardDirectory(){
        String SdcardPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        String dir = SdcardPath.substring(SdcardPath.lastIndexOf('/') + 1);
        System.out.println(dir);
        String[] trimmed = SdcardPath.split(dir);
        String sdcardPath = trimmed[0];
        System.out.println(sdcardPath);
        return sdcardPath;
    }

    @Override
    public void downloadVideo(String title) {
        txtSongTitle.setText(title.substring(0, title.indexOf(".")));
        httpsReference.child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(MainActivity.this, "Loading successful", Toast.LENGTH_SHORT).show();
                MediaController mediaController = new MediaController(MainActivity.this);
                mediaController.setAnchorView(karaokeView);
                karaokeView.setMediaController(mediaController);
                karaokeView.setVideoURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, "Loading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void swapActivity(String data) {
        //do nothing
    }

    private void mapping() {
        play = findViewById(R.id.btn_play_record);
        stop = findViewById(R.id.btn_stop_record);
        start = findViewById(R.id.btn_start_record);
        karaokeView = findViewById(R.id.video_karaoke);
        btnPlay = findViewById(R.id.imageButtonPlay);
        btnPlay.setEnabled(false);
        btnStop = findViewById(R.id.imageButtonStop);
        btnStop.setEnabled(false);
        btnReplay = findViewById(R.id.imageButtonReplay);
        btnReplay.setEnabled(false);
        btnSave = findViewById(R.id.imageButtonSave);
        btnSave.setEnabled(false);
        txtTimeSong = findViewById(R.id.txt_timeSong);
        txtTimeTotal = findViewById(R.id.txt_timeTotal);
        txtSongTitle = findViewById(R.id.txt_songTitle);
        seekBarSong = findViewById(R.id.seekBarSong);
        mediaPlayer = new MediaPlayer();
    }

    private void setTimeTotal() {
        SimpleDateFormat dinhDangTG = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(dinhDangTG.format(mediaPlayer.getDuration()));
        seekBarSong.setMax(mediaPlayer.getDuration());
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangTG = new SimpleDateFormat("mm:ss");
                txtTimeSong.setText(dinhDangTG.format(mediaPlayer.getCurrentPosition()));
                seekBarSong.setProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                        }
                        btnPlay.setImageResource(R.drawable.icon_play_button);
                    }
                });
                handler.postDelayed(this, 300);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onBackPressed();
    }
}
