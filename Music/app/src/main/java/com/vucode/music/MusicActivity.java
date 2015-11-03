package com.vucode.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    ListView lv;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.lvPlayerList);

        // Songs file location
        final ArrayList<File> songs = getSongs(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC));

        items = new String[songs.size()];

        // Remove file extention ending
        for(int i = 0; i < songs.size(); ++i) {
            items[i] = songs.get(i).getName().toString().replace(".mp3","").replace(".wav", "").replace(".m4a", "").replace(".wma","");
        }

        // Start the player activity
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout, R.id.textView, items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), PlayerActivity.class).putExtra("pos", position).putExtra("songList", songs));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
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

    // Get the songs from file array
    public ArrayList<File> getSongs(File root) {
        ArrayList<File> list = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files) {
            if(singleFile.isDirectory() && singleFile.isHidden()) {
                // Recurse through the directory
                list.addAll(getSongs((singleFile)));
            } else {
                // Get music files that end in ".mp3", ".wav", ".m4a" or ".wma"
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav") || singleFile.getName().endsWith(".m4a") || singleFile.getName().endsWith(".wma")) {
                    list.add(singleFile);
                }
            }
        }
        return list;
    }
}
