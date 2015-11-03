package com.vucode.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<File> mySongs;
    Uri u;
    Thread updateSeekBar;
    int position;
    static MediaPlayer mp;

    // Buttons
    SeekBar seekBar;
    Button buttonPrevious, buttonRewind, buttonPlay, buttonFastForward, buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set variables to id from layout\
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        buttonPrevious = (Button) findViewById(R.id.buttonPrevious);
        buttonRewind = (Button) findViewById(R.id.buttonRewind);
        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonFastForward = (Button) findViewById(R.id.buttonFastForward);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        // Listen for button click
        buttonPrevious.setOnClickListener(this);
        buttonRewind.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        buttonFastForward.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        // Update the seek bar
        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mp != null) {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songList");
        position = b.getInt("pos", 0);

        u =  Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        seekBar.setMax(mp.getDuration());
        updateSeekBar.start();

        // Seek bar position
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    // Button clicked
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonPrevious:
                mp.stop();
                mp.release();
                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                u =  Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
            case R.id.buttonRewind:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.buttonPlay:
                if(mp.isPlaying()) {
                    buttonPlay.setText("►");
                    mp.pause();
                } else {
                    mp.start();
                    buttonPlay.setText("||");
                }
                break;
            case R.id.buttonFastForward:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.buttonNext:
                mp.stop();
                mp.release();
                position = (position + 1) % mySongs.size();
                u =  Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                seekBar.setMax(mp.getDuration());
                break;
        }
    }
}
