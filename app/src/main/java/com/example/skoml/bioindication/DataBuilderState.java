package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.ecometr.app.R;

/**
 * Created by Developer on 5/20/2016.
 */
public abstract class DataBuilderState {

    private Bitmap stateImage = null;

    protected Paint paint = null;

    public DataBuilderState() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
    }

    public abstract void setPoint(LeafDataBuilder builder, Point point);

    public Point fixPointPosition(LeafDataBuilder builder, Point point) {
        return point;
    }

    public abstract boolean nextStep(LeafDataBuilder builder);

    protected abstract int getStateResourceId();

    public void drawStateImage(LeafDataBuilder builder, Canvas canvas) {
        if (stateImage == null)
            stateImage = builder.loadImage(getStateResourceId());
        canvas.drawBitmap(stateImage, 10, 10, null);
    }

    public void drawGuidelines(LeafDataBuilder builder, Canvas canvas) {
        // do nothing
    }
}

/**
 * 1. User specify bottom of the leaf
 */
class TopState extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setTop(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new BottomState());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_1;
    }
}

/**
 * 2. User specify top of the leaf
 */
class BottomState extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setBottom(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new CenterState());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_2;
    }
}

/**
 * 3. Finding the middle of the line
 * 4. Allow to move pointer athwart the middle line
 */
abstract class MiddleState extends DataBuilderState {

    @Override
    public Point fixPointPosition(LeafDataBuilder builder, Point point) {
        Point tmpPoint = super.fixPointPosition(builder, point);
        if (tmpPoint != null) {
            // fix current point position in the middle of leaf
            Point middle = builder.getLeafMiddle();
            int distance = builder.leafData.getDistance(middle, point);
            tmpPoint = builder.getLeafSidePoint(distance);
        };
        return tmpPoint;
    }

    @Override
    public void drawGuidelines(LeafDataBuilder builder, Canvas canvas) {
        super.drawGuidelines(builder, canvas);
        Point middle = builder.getLeafMiddle();
        Point left = builder.getLeafSidePoint(-1000);
        Point right = builder.getLeafSidePoint(1000);

        Path path = new Path();
        path.moveTo(builder.getTop().x, builder.getTop().y);
        path.lineTo(builder.getBottom().x, builder.getBottom().y);
        path.moveTo(middle.x, middle.y);
        path.lineTo(left.x, left.y);
        path.moveTo(middle.x, middle.y);
        path.lineTo(right.x, right.y);
        canvas.drawPath(path, paint);
    }
}

/**
 * 5. User specify the central vein of the leaf
 */
class CenterState extends MiddleState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setCenter(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new LeftSide());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_5;
    }
}

/**
 * 6. Allow move on the center line
 * 7. User specifies leftmost point of the leaf
 */
class LeftSide extends MiddleState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setLeft(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new RightSide());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_7;
    }
}

/**
 * 8. User specifies rightmost point of the leaf
 */
class RightSide extends MiddleState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setRight(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstLeftVeinBegin());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_8;
    }
}

/**
 * 9. Allow free movement
 * 10. User specify beginning of first left vein
 */
class FirstLeftVeinBegin extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setFirstLeftVeinBegin(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstLeftVeinEnd());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_10;
    }
}

/**
 * 11. User specify ending of first left vein
 */
class FirstLeftVeinEnd extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setFirstLeftVeinEnd(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstRightVeinBegin());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_11;
    }
}

/**
 * 12. User specify beginning of first right vein
 */
class FirstRightVeinBegin extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setFirstRightVeinBegin(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstRightVeinEnd());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_12;
    }
}

/**
 * 13. User specify ending of first right vein
 */
class FirstRightVeinEnd extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setFirstRightVeinEnd(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondLeftVeinBegin());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_13;
    }
}

/**
 * 14. User spcify beginning of second left vein
 */
class SecondLeftVeinBegin extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setSecondLeftVeinBegin(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondLeftVeinEnd());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_14;
    }
}

/**
 * 15. User specify ending of second left vein
 */
class SecondLeftVeinEnd extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setSecondLeftVeinEnd(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondRightVeinBegin());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_15;
    }
}

/**
 * 16. User specify beginning of second right vein
 */
class SecondRightVeinBegin extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setSecondRightVeinBegin(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondRightVeinEnd());
        return true;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_16;
    }
}

/**
 * 17. User specify ending of second right vein
 */
class SecondRightVeinEnd extends  DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.setSecondRightVeinEnd(point);
    }

    @Override
    public boolean nextStep(LeafDataBuilder builder) {
        return false;
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_17;
    }
}