package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Developer on 5/19/2016.
 */
public class LeafData {

    private Bitmap leafImage;

    public LeafData(Bitmap leafImage) {
        this.leafImage = leafImage;
    }

    public Bitmap getImage() {
        return leafImage;
    }

    public int getWidth() {
        return leafImage.getWidth();
    }

    public int getHeight() {
        return leafImage.getHeight();
    }

}
