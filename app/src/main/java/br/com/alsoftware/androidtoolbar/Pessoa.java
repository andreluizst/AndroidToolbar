package br.com.alsoftware.androidtoolbar;

import com.mikepenz.materialdrawer.model.ProfileDrawerItem;


public class Pessoa {
    private ProfileDrawerItem profile;
    private int background;


    public Pessoa(){

    }

    public ProfileDrawerItem getProfile() {
        return profile;
    }

    public void setProfile(ProfileDrawerItem profile) {
        this.profile = profile;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }
}
