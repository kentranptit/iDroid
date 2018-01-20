package com.idroid.pakandroid.ptit.singnow;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by admin on 1/20/2018.
 */

public class ShowListSongActivity extends AppCompatActivity implements IGiaoTiep {
    ArrayList<Song> listSong;
    SongAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DatabaseReference mData;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference httpsReference = storage.getReference();

    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        init();

        adapter = new SongAdapter(ShowListSongActivity.this, listSong, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

//        addData();
        loadSongData();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView_song);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        listSong = new ArrayList<>();
        videoView = findViewById(R.id.videoView);
        mData = FirebaseDatabase.getInstance().getReference();
    }

    private void loadSongData() {
        mData.child("Songs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Song song = dataSnapshot.getValue(Song.class);
                listSong.add(new Song(song.getName(), song.getSinger(), song.getLink()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addData() {
        Song song = new Song("Người lạ ơi", "Superbrothers x Karik x Orange", "https://firebasestorage.googleapis.com/v0/b/sing-now-80870.appspot.com/o/Ng%C6%B0%E1%BB%9Di%20l%E1%BA%A1%20%C6%A1i_Superbrothers%20x%20Karik%20x%20Orange.MP4?alt=media&token=2c10101e-d3b9-43aa-a501-edc28ff6c4cf");
        mData.child("Songs").push().setValue(song, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(ShowListSongActivity.this, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShowListSongActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void downloadVideo(String title) {
        httpsReference.child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(ShowListSongActivity.this, "Download thanh cong", Toast.LENGTH_SHORT).show();
                MediaController mediaController = new MediaController(ShowListSongActivity.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.start();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ShowListSongActivity.this, "That bai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void swapActivity() {
        Intent swapToMainActIntent = new Intent(ShowListSongActivity.this, MainActivity.class);
        startActivity(swapToMainActIntent);
    }
}
