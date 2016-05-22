package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by Developer on 5/20/2016.
 */
public class LeafDataBuilder {
    public LeafData leafData = null;
    private DataBuilderState state = null;
    private Context context = null;

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

    public int getDistance(Point a, Point b) {
        int d = (int) Math.round(Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)));
        if ((a.x < b.x) && (a.y < b.y))
            d = -d;
        return d;
    }

    public Point getLeafMiddle() {
        if ((top != null) && (bottom != null)) {
            Point p = new Point();
            p.x = top.x + ((bottom.x - top.x) / 2);
            p.y = top.y + ((bottom.y - top.y) / 2);
            return p;
        } else
            return null;
    }

    public Point getLeafSidePoint(int distance) {
        double angle;
        if ((bottom.x - top.x) != 0)
            angle = Math.atan((top.y - bottom.y) / (top.x - bottom.x)) - (Math.PI / 2);
        else
            angle = -(Math.PI / 2);

        Point p = new Point();
        Point m = getLeafMiddle();
        p.x = (int) Math.round(m.x + distance * Math.cos(angle));
        p.y = (int) Math.round(m.y + distance * Math.sin(angle));
        return p;
    }

    public LeafDataBuilder(LeafData leafData, Context context) {
        this.leafData = leafData;
        this.context = context;
        setState(new TopState());
    }

    public void setState(DataBuilderState state) {
        this.state = state;
    }

    public void processPoint(Point point) {
        state.setPoint(this, point);
        state.nextStep(this);
    }

    public Bitmap loadImage(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public Point fixPointPosition(Point point) {
        return state.fixPointPosition(this, point);
    }

    public void logPoint(String name, Point point) {
        Log.v(name, "(" + Integer.toString(point.x) + ", " + Integer.toString(point.y) + ")");
    }

    public void drawState(Canvas canvas) {
        canvas.drawBitmap(leafData.getImage(), 0, 0, null);
        canvas.drawBitmap(state.getStateImage(this), 10, 10, null);
    }
}
