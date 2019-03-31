package com.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TimeLine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
    }


    public void timelineCreate(){
        int timelineId;
        String timeStamp;
        String description;
        timelineId = 1;
        timeStamp ="05:00";
        description = "Suck";

        System.out.println("SHIT");

        try{
            File file = new File(getExternalFilesDir(null), "timeline.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write(timeStamp.getBytes());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
