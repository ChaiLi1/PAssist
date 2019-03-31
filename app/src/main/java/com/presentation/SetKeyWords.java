package com.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SetKeyWords extends AppCompatActivity {

    final ArrayList<String> kw = new ArrayList<String>();
    // concat all the key words
    public String arrayData(ArrayList<String> array){
        String result = "";
        for(int i = 0;i<array.size();i++){
            result += array.get(i)+"\n";
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_key_words);

        Button btn1 = (Button) findViewById(R.id.sub);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.editText1);
                String content = editText1.getText().toString();

                kw.add(content);

                SharedPreferences userSettings = getSharedPreferences("setting", 0);    // use SharedPreferences to store the key words
                SharedPreferences.Editor editor = userSettings.edit();
                editor.putString("keyWords", arrayData(kw));
                editor.commit();

                AlertDialog alertDialog = new AlertDialog.Builder(SetKeyWords.this)
                        .setTitle("Information")
                        .setMessage("Set key words success")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(SetKeyWords.this, "This is the confirm button", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog.show();
                editText1.setText("");  // clear the editText

                TextView textView = (TextView)findViewById(R.id.displayData);
                textView.setText(arrayData(kw));
            }
        });

    }
}
