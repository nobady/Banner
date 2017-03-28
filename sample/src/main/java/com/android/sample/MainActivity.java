package com.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        findViewById (R.id.xml_btn).setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                Intent intent = new Intent (MainActivity.this,XmlShowActivity.class);
                startActivity (intent);
            }
        });
        findViewById (R.id.code_btn).setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View v) {
                Intent intent = new Intent (MainActivity.this,CodeShowActivity.class);
                startActivity (intent);
            }
        });
    }
}
