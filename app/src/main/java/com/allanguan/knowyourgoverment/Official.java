package com.allanguan.knowyourgoverment;

import java.io.Serializable;

public class Official implements Serializable {
    String name;
    String title;
    String party;
    String address;
    String phone;
    String email;
    String website;
    String photoURL;
    String fbID;
    String twID;
    String ytID;


//    public Official(String t, String n){
//        this.name = n;
//        this.title = t;
//        this.party = "party";
//    }

    public Official(String name, String title, String party, String address, String phone, String email, String website, String photoURL, String fbID, String twID, String ytID) {
        this.name = name;
        this.title = title;
        this.party = party;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.photoURL = photoURL;
        this.fbID = fbID;
        this.twID = twID;
        this.ytID = ytID;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getTwID() {
        return twID;
    }

    public void setTwID(String twID) {
        this.twID = twID;
    }

    public String getYtID() {
        return ytID;
    }

    public void setYtID(String ytID) {
        this.ytID = ytID;
    }

}
