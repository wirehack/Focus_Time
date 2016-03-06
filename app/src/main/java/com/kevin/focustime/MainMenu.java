package com.kevin.focustime;


/**
 * Created by lenovo on 2015-07-27.
 */
public class MainMenu {
    private int imageId;
    private String name;

    public MainMenu(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }
}
