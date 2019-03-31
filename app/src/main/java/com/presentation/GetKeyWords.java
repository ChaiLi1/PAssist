package com.presentation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GetKeyWords extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_key_words);

        Button btn1 = (Button)findViewById(R.id.display);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView)findViewById(R.id.textView);

                SharedPreferences userSettings= getSharedPreferences("setting", 0);
                String keyWords = userSettings.getString("keyWords","default");

                textView.setText(keyWords);
            }
        });
    }
}
