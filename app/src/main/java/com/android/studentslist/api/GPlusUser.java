package com.android.studentslist.api;

public class GPlusUser {
    private String displayName;
    private Image image;

    private class Image {
        private String url;
    }

    public String getFullName() {
        return displayName;
    }

    public String getImageUrl() {
        return image.url.substring(0, image.url.lastIndexOf("?"));
    }
}
