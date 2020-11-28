package com.allanguan.knowyourgoverment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PhotoActivity extends AppCompatActivity {

    private Official o;
    private TextView location;
    private TextView title;
    private TextView name;

    private ImageView photo;
    private ImageView partyImage;
    private View bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Intent data = getIntent();
        o = (Official)data.getSerializableExtra("oObj");

        location = findViewById(R.id.photoLocation);
        title = findViewById(R.id.photoTitle);
        name = findViewById(R.id.photoName);

        photo = findViewById(R.id.photoImage);
        partyImage = findViewById(R.id.photoLogo);
        bg = findViewById(R.id.photoBG);

        title.setText(o.getTitle());
        name.setText(o.getName());


        if(!o.getPhotoURL().isEmpty()) {
            Picasso.get().load(o.getPhotoURL())
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(photo);
        }

        if(o.getParty().contains("Republic")){
            partyImage.setImageResource(R.drawable.rep_logo);
            bg.setBackgroundColor(Color.parseColor("RED")); }

        if(o.getParty().contains("Democr")){
            partyImage.setImageResource(R.drawable.dem_logo);
            bg.setBackgroundColor(Color.parseColor("BLUE")); }

        if (o.getParty().contains("Nonpartisan")){
            partyImage.setVisibility(View.GONE);
            bg.setBackgroundColor(Color.parseColor("BLACK"));}

        if (o.getParty().contains("Unknown")){
            partyImage.setVisibility(View.GONE);
            bg.setBackgroundColor(Color.parseColor("BLACK"));}
    }
}