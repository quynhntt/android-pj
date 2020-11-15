package com.notpad.medate.utils;

public class Person {
    private String meowID;
    private String name;
    private String profileImageUrl;

    public Person() {
    }

    public Person(String meowID, String name, String profileImageUrl) {
        this.meowID = meowID;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getMeowID() {
        return meowID;
    }

    public void setMeowID(String meowID) {
        this.meowID = meowID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }


}
