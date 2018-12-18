package com.universidad.bluenet.opencvagain;

/**
 * Created by Pc on 7/12/2018.
 */

public class itemPersonal {
    String key;
    String profile_name;
    int profilePhoto;

    public itemPersonal(String key, String profile_name, int profilePhoto) {
        this.key = key;
        this.profile_name = profile_name;
        this.profilePhoto = profilePhoto;
    }
    public String getProfile_name() {
        return profile_name;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public String getKey() {
        return key;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
