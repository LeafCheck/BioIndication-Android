package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

/**
 * Created by Developer on 5/19/2016.
 */
public class LeafData {

    public class Calculation {
        public double left;
        public double right;

        public Calculation(double left, double right) {
            this.left = left;
            this.right = right;
        }

        public double value() {
            return (left - right) / (left + right);
        }
    }

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

    public Calculation sideWidth() {
        return new Calculation(getDistance(center, left), getDistance(center, right));
    }

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

    public int getDistance(Point a, Point b) {
        int d = (int) Math.round(Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)));
        if ((a.x < b.x) && (a.y < b.y))
            d = -d;
        return d;
    }
}
