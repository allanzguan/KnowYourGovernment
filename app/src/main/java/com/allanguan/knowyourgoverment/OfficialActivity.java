package com.allanguan.knowyourgoverment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private Official o;
    TextView title;
    TextView name;
    TextView party;
    TextView address;
    TextView phone;
    TextView email;
    TextView website;
    TextView aLabel;
    TextView pLabel;
    TextView eLabel;
    TextView wLabel;


    ScrollView sv;
    TextView loc;

    ImageView partyImage;
    ImageView photo;
    ImageView fb;
    ImageView tw;
    ImageView yt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);

        loc = findViewById(R.id.location);
        name = findViewById(R.id.name);
        title = findViewById(R.id.title);
        party = findViewById(R.id.party);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        website = findViewById(R.id.website);
        aLabel = findViewById(R.id.addressLabel);
        pLabel = findViewById(R.id.phoneLabel);
        eLabel = findViewById(R.id.emailLabel);
        wLabel = findViewById(R.id.websiteLabel);
        fb = findViewById(R.id.fb);
        tw = findViewById(R.id.tw);
        yt = findViewById(R.id.yt);
        photo = findViewById(R.id.officialImage);
        sv = findViewById(R.id.scroll);

        partyImage = findViewById(R.id.partyImage);

        if(getIntent().hasExtra("OBJ")){
            o = (Official) getIntent().getSerializableExtra("OBJ");

            loc.setText(getIntent().getStringExtra("LOC"));

            title.setText(o.getTitle());
            name.setText(o.getName());
            address.setText(o.getAddress());
            phone.setText(o.getPhone());
            email.setText(o.getEmail());
            website.setText(o.getWebsite());

            if(o.getParty().contains("Republic")){
                party.setText("("+o.getParty()+")");
                partyImage.setImageResource(R.drawable.rep_logo);
                sv.setBackgroundColor(Color.parseColor("RED")); }

            if(o.getParty().contains("Democr")){
                party.setText("("+o.getParty()+")");
                partyImage.setImageResource(R.drawable.dem_logo);
                sv.setBackgroundColor(Color.parseColor("BLUE")); }

            if (o.getParty().contains("Nonpartisan")){
                party.setText("(Nonpartisan)");
                partyImage.setVisibility(View.GONE);
                sv.setBackgroundColor(Color.parseColor("BLACK"));}

            if (o.getParty().contains("Unknown")){
                party.setText("(Unknown)");
                partyImage.setVisibility(View.GONE);
                sv.setBackgroundColor(Color.parseColor("BLACK"));}

            if(o.getAddress().isEmpty()){
                address.setVisibility(View.GONE);
                aLabel.setVisibility(View.GONE); }

            if (o.getPhone().isEmpty()){
                phone.setVisibility(View.GONE);
                pLabel.setVisibility(View.GONE); }

            if(o.getEmail().isEmpty()){
                email.setVisibility(View.GONE);
                eLabel.setVisibility(View.GONE); }

            if(o.getWebsite().isEmpty()){
                website.setVisibility(View.GONE);
                wLabel.setVisibility(View.GONE); }

            if(o.getFbID().isEmpty()){
                fb.setVisibility(View.GONE); }

            if(o.getTwID().isEmpty()){
                tw.setVisibility(View.GONE); }

            if(o.getYtID().isEmpty()){
                yt.setVisibility(View.GONE); }

            if(!o.getPhotoURL().isEmpty()) {
                Picasso.get().load(o.getPhotoURL())
                        .error(R.drawable.brokenimage)
                        .placeholder(R.drawable.placeholder)
                        .into(photo);
            }

            Linkify.addLinks(address, Linkify.ALL);
            address.setLinkTextColor(Color.parseColor("White"));
            Linkify.addLinks(phone, Linkify.ALL);
            phone.setLinkTextColor(Color.parseColor("White"));
            Linkify.addLinks(email, Linkify.ALL);
            email.setLinkTextColor(Color.parseColor("White"));
            Linkify.addLinks(website, Linkify.ALL);
            website.setLinkTextColor(Color.parseColor("White"));
        }
    }

    public void imageClick(View v) {
//        Toast.makeText(this, "You clicked an Image", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "imageClick: "+photo.getDrawable().getConstantState().toString());
        if(!o.getPhotoURL().isEmpty()){
            Intent data = new Intent(this, PhotoActivity.class);
            data.putExtra("oObj", o);
            startActivity(data);
        }
        else{
            return;
        }

    }

    public void logoClick(View v) {
//        Toast.makeText(this, "You clicked an Logo", Toast.LENGTH_SHORT).show();
        String repURL = "https://www.gop.com";
        String demURL = "https://democrats.org";
        Intent i = new Intent(Intent.ACTION_VIEW);
        if(o.getParty().contains("Republic")){
            i.setData(Uri.parse(repURL));
            startActivity(i);
        }
        if(o.getParty().contains("Democr")){
            i.setData(Uri.parse(demURL));
            startActivity(i);
        }
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = o.getTwID();
        try {
        // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
        // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + o.getFbID();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + o.getFbID();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void youTubeClicked(View v) {
        String name = o.getYtID();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + name)));
        }
    }
}
