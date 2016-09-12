package com.webster.gmobile.mockdata;

/**
 * Created by weby on 1/2/2016.
 */
public enum ImageSize {
    small(360),
    large(1080),
    hd(1980);
    final int width;

    ImageSize(int width) {
        this.width = width;
    }
}
