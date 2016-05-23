package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by Developer on 5/19/2016.
 */
public class LeafData {

    // key points
    public Point top = null;
    public Point bottom = null;
    public Point center = null;
    public Point right = null;
    public Point left = null;
    public Point firstLeftVeinBegin = null;
    public Point firstLeftVeinEnd = null;
    public Point firstRightVeinBegin = null;
    public Point firstRightVeinEnd = null;
    public Point secondLeftVeinBegin = null;
    public Point secondLeftVeinEnd = null;
    public Point secondRightVeinBegin = null;
    public Point secondRightVeinEnd = null;

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
