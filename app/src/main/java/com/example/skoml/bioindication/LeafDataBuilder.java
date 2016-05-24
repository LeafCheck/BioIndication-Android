package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Developer on 5/20/2016.
 */
public class LeafDataBuilder {
    public LeafData leafData = null;
    private DataBuilderState state = null;
    private Context context = null;
    private Matrix leafMatrix = new Matrix();

    public int getDistance(Point a, Point b) {
        int d = (int) Math.round(Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2)));
        if ((a.x < b.x) && (a.y < b.y))
            d = -d;
        return d;
    }

    public Point getLeafMiddle() {
        if ((getTop() != null) && (getBottom() != null)) {
            Point p = new Point();
            p.x = getTop().x + ((getBottom().x - getTop().x) / 2);
            p.y = getTop().y + ((getBottom().y - getTop().y) / 2);
            return p;
        } else
            return null;
    }

    public Point getLeafSidePoint(int distance) {
        double angle;
        if ((getBottom().x - getTop().x) != 0)
            angle = Math.atan((getTop().y - getBottom().y) / (getTop().x - getBottom().x)) - (Math.PI / 2);
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
        leafMatrix.postTranslate(200, 200);
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
        canvas.drawBitmap(leafData.getImage(), leafMatrix, null);
        canvas.drawBitmap(state.getStateImage(this), 10, 10, null);
    }

    private Point MapPoint(Point p) {
        Matrix inv = new Matrix();
        leafMatrix.invert(inv);
        float[] point = {p.x, p.y};
        inv.mapPoints(point);
        Point out = new Point();
        out.x = (int)point[0];
        out.y = (int)point[1];
        return out;
    }

    public Point getTop() {
        return leafData.top;
    }

    public void setTop(Point top) {
        this.leafData.top = MapPoint(top);
    }

    public Point getBottom() {
        return leafData.bottom;
    }

    public void setBottom(Point bottom) {
        this.leafData.bottom = bottom;
    }

    public Point getCenter() {
        return leafData.center;
    }

    public void setCenter(Point center) {
        this.leafData.center = center;
    }

    public Point getRight() {
        return leafData.right;
    }

    public void setRight(Point right) {
        this.leafData.right = right;
    }

    public Point getLeft() {
        return leafData.left;
    }

    public void setLeft(Point left) {
        this.leafData.left = left;
    }

    public Point getFirstLeftVeinBegin() {
        return leafData.firstLeftVeinBegin;
    }

    public void setFirstLeftVeinBegin(Point firstLeftVeinBegin) {
        this.leafData.firstLeftVeinBegin = firstLeftVeinBegin;
    }

    public Point getFirstLeftVeinEnd() {
        return leafData.firstLeftVeinEnd;
    }

    public void setFirstLeftVeinEnd(Point firstLeftVeinEnd) {
        this.leafData.firstLeftVeinEnd = firstLeftVeinEnd;
    }

    public Point getFirstRightVeinBegin() {
        return leafData.firstRightVeinBegin;
    }

    public void setFirstRightVeinBegin(Point firstRightVeinBegin) {
        this.leafData.firstRightVeinBegin = firstRightVeinBegin;
    }

    public Point getFirstRightVeinEnd() {
        return leafData.firstRightVeinEnd;
    }

    public void setFirstRightVeinEnd(Point firstRightVeinEnd) {
        this.leafData.firstRightVeinEnd = firstRightVeinEnd;
    }

    public Point getSecondLeftVeinBegin() {
        return leafData.secondLeftVeinBegin;
    }

    public void setSecondLeftVeinBegin(Point secondLeftVeinBegin) {
        this.leafData.secondLeftVeinBegin = secondLeftVeinBegin;
    }

    public Point getSecondLeftVeinEnd() {
        return leafData.secondLeftVeinEnd;
    }

    public void setSecondLeftVeinEnd(Point secondLeftVeinEnd) {
        this.leafData.secondLeftVeinEnd = secondLeftVeinEnd;
    }

    public Point getSecondRightVeinBegin() {
        return leafData.secondRightVeinBegin;
    }

    public void setSecondRightVeinBegin(Point secondRightVeinBegin) {
        this.leafData.secondRightVeinBegin = secondRightVeinBegin;
    }

    public Point getSecondRightVeinEnd() {
        return leafData.secondRightVeinEnd;
    }

    public void setSecondRightVeinEnd(Point secondRightVeinEnd) {
        this.leafData.secondRightVeinEnd = secondRightVeinEnd;
    }
}
