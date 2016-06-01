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
    }

    public void setState(DataBuilderState state) {
        this.state = state;
    }

    public boolean processPoint(Point point) {
        state.setPoint(this, point);
        return state.nextStep(this);
    }

    public Bitmap loadImage(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public Point adjustPointerPosition(Point point) {
        return state.fixPointPosition(this, point);
    }

    public void logPoint(String name, Point point) {
        Log.v(name, "(" + Integer.toString(point.x) + ", " + Integer.toString(point.y) + ")");
    }

    public void drawState(Canvas canvas) {
        canvas.drawBitmap(leafData.getImage(), leafMatrix, null);
        state.drawStateImage(this, canvas);
        state.drawGuidelines(this, canvas);
    }

    public void panLeafImage(int x, int y) {
        leafMatrix.postTranslate(x, y);
    }

    private Point MapPoint(Point p, Boolean inverse) {
        if (p == null) return new Point();

        Matrix m = new Matrix();
        if (inverse)
            leafMatrix.invert(m);
        else
            m = leafMatrix;
        float[] point = {p.x, p.y};
        m.mapPoints(point);
        Point out = new Point();
        out.x = (int)point[0];
        out.y = (int)point[1];
        return out;
    }

    private Point getPoint(Point p) {
        return MapPoint(p, false);
    }

    private Point setPoint(Point p) {
        return MapPoint(p, true);
    }

    public Point getTop() {
        return getPoint(leafData.top);
    }

    public void setTop(Point top) {
        this.leafData.top = setPoint(top);
    }

    public Point getBottom() {
        return getPoint(leafData.bottom);
    }

    public void setBottom(Point bottom) {
        this.leafData.bottom = setPoint(bottom);
    }

    public Point getCenter() {
        return getPoint(leafData.center);
    }

    public void setCenter(Point center) {
        this.leafData.center = setPoint(center);
    }

    public Point getRight() {
        return getPoint(leafData.right);
    }

    public void setRight(Point right) {
        this.leafData.right = setPoint(right);
    }

    public Point getLeft() {
        return getPoint(leafData.left);
    }

    public void setLeft(Point left) {
        this.leafData.left = setPoint(left);
    }

    public Point getFirstLeftVeinBegin() {
        return getPoint(leafData.firstLeftVeinBegin);
    }

    public void setFirstLeftVeinBegin(Point firstLeftVeinBegin) {
        this.leafData.firstLeftVeinBegin = setPoint(firstLeftVeinBegin);
    }

    public Point getFirstLeftVeinEnd() {
        return getPoint(leafData.firstLeftVeinEnd);
    }

    public void setFirstLeftVeinEnd(Point firstLeftVeinEnd) {
        this.leafData.firstLeftVeinEnd = setPoint(firstLeftVeinEnd);
    }

    public Point getFirstRightVeinBegin() {
        return leafData.firstRightVeinBegin;
    }

    public void setFirstRightVeinBegin(Point firstRightVeinBegin) {
        this.leafData.firstRightVeinBegin = getPoint(firstRightVeinBegin);
    }

    public Point getFirstRightVeinEnd() {
        return leafData.firstRightVeinEnd;
    }

    public void setFirstRightVeinEnd(Point firstRightVeinEnd) {
        this.leafData.firstRightVeinEnd = setPoint(firstRightVeinEnd);
    }

    public Point getSecondLeftVeinBegin() {
        return getPoint(leafData.secondLeftVeinBegin);
    }

    public void setSecondLeftVeinBegin(Point secondLeftVeinBegin) {
        this.leafData.secondLeftVeinBegin = setPoint(secondLeftVeinBegin);
    }

    public Point getSecondLeftVeinEnd() {
        return getPoint(leafData.secondLeftVeinEnd);
    }

    public void setSecondLeftVeinEnd(Point secondLeftVeinEnd) {
        this.leafData.secondLeftVeinEnd = setPoint(secondLeftVeinEnd);
    }

    public Point getSecondRightVeinBegin() {
        return getPoint(leafData.secondRightVeinBegin);
    }

    public void setSecondRightVeinBegin(Point secondRightVeinBegin) {
        this.leafData.secondRightVeinBegin = setPoint(secondRightVeinBegin);
    }

    public Point getSecondRightVeinEnd() {
        return getPoint(leafData.secondRightVeinEnd);
    }

    public void setSecondRightVeinEnd(Point secondRightVeinEnd) {
        this.leafData.secondRightVeinEnd = setPoint(secondRightVeinEnd);
    }
}
