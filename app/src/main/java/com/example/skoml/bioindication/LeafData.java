package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by Developer on 5/19/2016.
 */
public class LeafData {

    private float scale = 1;

    public LeafData(Bitmap leafImage) {
        this.leafImage = leafImage;
        scale = leafImage.getWidth() / 100;
    }

    public float getScale() {
        return scale;
    }

    public class Calculation {
        public double left;
        public double right;

        public Calculation(double left, double right) {
            this.left = left;
            this.right = right;
        }

        public double getValue() {
            return Math.abs(left - right) / (left + right);
        }
    }

    public class  Line {
        public Point A = null;
        public Point B = null;

        public Line(Point A, Point B) {
            this.A = A;
            this.B = B;
        }

        public double length() {
            return Math.round(Math.sqrt(Math.pow(B.x - A.x, 2) + Math.pow(B.y - A.y, 2)));
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
        return new Calculation(new Line(center, left).length() / scale,
                new Line(center, right).length() / scale);
    }

    public Calculation secondVeinLength() {
        return new Calculation(new Line(secondLeftVeinBegin,secondLeftVeinEnd).length() / scale,
                new Line(secondRightVeinBegin, secondRightVeinEnd).length() / scale);
    }

    public Calculation veinBaseDistance() {
        return new Calculation(new Line(firstLeftVeinBegin, secondLeftVeinBegin).length() / scale,
                new Line(firstRightVeinBegin, secondRightVeinBegin).length() / scale);
    }

    public Calculation veinEndingDistance() {
        return new Calculation(new Line(firstLeftVeinEnd, secondLeftVeinEnd).length() / scale,
                new Line(firstRightVeinEnd, secondRightVeinEnd).length() / scale);
    }

    public Calculation secondVeinAngle() {
        return new Calculation(getAngle(new Line(secondLeftVeinBegin, secondLeftVeinEnd), new Line(bottom, top)),
                getAngle(new Line(secondRightVeinBegin, secondRightVeinEnd), new Line(bottom, top)));
    }

    public double getValue() {
        return (sideWidth().getValue() + secondVeinLength().getValue() +
                veinBaseDistance().getValue() + veinEndingDistance().getValue()
                + secondVeinAngle().getValue()) / 5;
    }

    private Bitmap leafImage;

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
        if (a.x < b.x)
            d = -d;
        return d;
    }

    public double getAngle(Line line1, Line line2) {
        double angle1 = Math.toDegrees(Math.atan2(Math.abs(line1.A.y - line1.B.y), Math.abs(line1.A.x - line1.B.x)));
        double angle2 = Math.toDegrees(Math.atan2(Math.abs(line2.A.y - line2.B.y), Math.abs(line2.A.x - line2.B.x)));
        return Math.abs(angle1 - angle2);
    }
}
