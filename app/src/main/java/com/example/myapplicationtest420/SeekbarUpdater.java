package com.example.myapplicationtest420;

import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekbarUpdater implements Runnable{

    private int duration, currentTime;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private TextView currentTimeText;
    private boolean exit;
    private PlayerActivity playerActivity;
    Thread thread;


    public SeekbarUpdater( PlayerActivity playerActivity){
        this.currentTime = 0;
        this.duration = playerActivity.durationValue;
        this.seekBar = playerActivity.seekBar;
        this.mediaPlayer = playerActivity.mediaPlayer;
        this.exit = false;
        this.currentTimeText = playerActivity.currentTime;
        this.playerActivity = playerActivity;
        playerActivity.duration.setText(createTime(duration));
        thread = new Thread(this, "thread");
        thread.start();
    }

    @Override
    public void run() {
        while (!exit){
            if(currentTime < duration){
                try {
                    Thread.sleep(500);
                    currentTime = mediaPlayer.getCurrentPosition();
                    playerActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(currentTime);
                            currentTimeText.setText(createTime(currentTime));
                        }
                    });

                } catch (InterruptedException | IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public String createTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if (sec<10)
        {
            time+="0";
        }
        time+=sec;

        return  time;
    }

    public void close(){
        exit = true;
    }
}
