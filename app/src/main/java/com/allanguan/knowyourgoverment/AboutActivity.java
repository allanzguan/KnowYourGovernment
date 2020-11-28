package com.allanguan.knowyourgoverment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView api = findViewById(R.id.api);
        api.setPaintFlags(api.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void apiClicked(View v){
        String url = "https://developers.google.com/civic-information/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}