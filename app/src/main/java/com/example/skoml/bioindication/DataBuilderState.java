package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.ecometr.app.R;

/**
 * Created by Developer on 5/20/2016.
 */
public abstract class DataBuilderState {
    private Bitmap stateImage = null;

    public abstract void setPoint(LeafDataBuilder builder, Point point);

    public Point fixPointPosition(LeafDataBuilder builder, Point point) {
        return point;
    }

    public abstract void nextStep(LeafDataBuilder builder);

    public final Bitmap getStateImage(LeafDataBuilder builder) {
        if (stateImage == null)
            stateImage = builder.loadImage(getStateResourceId());
        return stateImage;
    }

    protected abstract int getStateResourceId();
}

/**
 * 1. User specify bottom of the leaf
 */
class TopState extends DataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.top = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new BottomState());
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
        builder.bottom = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new CenterState());
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
            int distance = builder.getDistance(middle, point);
            return builder.getLeafSidePoint(distance);
        }
        return tmpPoint;
    }
}

/**
 * 5. User specify the central vein of the leaf
 */
class CenterState extends MiddleState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.center = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new LeftSide());
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
        builder.left = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new RightSide());
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
        builder.right = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstLeftVeinBegin());
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
        builder.firstLeftVeinBegin = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstLeftVeinEnd());
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
        builder.firstLeftVeinEnd = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstRightVeinBegin());
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
        builder.firstRightVeinBegin = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new FirstRightVeinEnd());
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
        builder.firstRightVeinEnd = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondLeftVeinBegin());
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
        builder.secondLeftVeinBegin = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondLeftVeinEnd());
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
        builder.secondLeftVeinEnd = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondRightVeinBegin());
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
        builder.secondRightVeinBegin = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new SecondRightVeinEnd());
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
        builder.secondRightVeinEnd = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {

    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_17;
    }
}