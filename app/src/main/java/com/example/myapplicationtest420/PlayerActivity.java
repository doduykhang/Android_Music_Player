package com.example.myapplicationtest420;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button buttonPlay, buttonRewind, buttonPrevious, buttonForward, buttonNext;
    TextView songTitle, currentTime, duration;
    SeekBar seekBar;
    BarVisualizer barVisualizer;

    SeekbarUpdater seekbarUpdater;
    MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    boolean exit = false;
    int currentTimeValue = 0, durationValue = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        init();
        initMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        if(seekbarUpdater != null)
            seekbarUpdater.close();
        if(barVisualizer != null)
            barVisualizer.release();

    }

    public void onPlayButtonClick(View view) {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            buttonPlay.setBackgroundResource(R.drawable.ic_baseline_play_arrow);
        }
        else{
            mediaPlayer.start();
            buttonPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
        }
    }

    public void init(){
        buttonPlay = findViewById(R.id.playButton);
        buttonRewind = findViewById(R.id.fastRewindButton);
        buttonPrevious = findViewById(R.id.skipPrevious);
        buttonForward = findViewById(R.id.fastForward);
        buttonNext = findViewById(R.id.skipNext);
        songTitle = findViewById(R.id.titleText);
        currentTime = findViewById(R.id.currentTime);
        duration = findViewById(R.id.duration);
        seekBar = findViewById(R.id.seekBar);
        barVisualizer = findViewById(R.id.blast);
    }

    public void initMediaPlayer(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        position = bundle.getInt("pos",0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        playSong();
    }



    public void onNextButtonClick(View view) {

        position = (position+1)%mySongs.size();
        playSong();
    }

    public void onPreviousButtonClick(View view) {

        position = (position-1 < 0) ? mySongs.size()-1:position-1;
        playSong();
    }

    public void playSong(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        if(seekbarUpdater != null)
            seekbarUpdater.close();


        currentTimeValue = 0;
        durationValue = 0;
        Uri uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        String songName = mySongs.get(position).getName();
        songTitle.setText(songName);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        durationValue = mediaPlayer.getDuration();
        seekbarUpdater = new SeekbarUpdater(this);
        int audioSessionId = mediaPlayer.getAudioSessionId();
        if(audioSessionId != -1)
            barVisualizer.setAudioSessionId(audioSessionId);


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                buttonNext.performClick();
            }
        });

        buttonPlay.setBackgroundResource(R.drawable.ic_baseline_pause_24);
    }

    public void onForwardButtonClick(View view) {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5*1000);
    }

    public void onRewindButtonClick(View view) {
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5*1000);
    }
}
