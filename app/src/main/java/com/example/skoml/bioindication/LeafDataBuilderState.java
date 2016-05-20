package com.example.skoml.bioindication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.ecometr.app.R;

/**
 * Created by Developer on 5/20/2016.
 */
public abstract class LeafDataBuilderState {
    private Bitmap stateImage = null;

    public abstract void setPoint(LeafDataBuilder builder, Point point);
    public abstract void nextStep(LeafDataBuilder builder);
    public Bitmap getStateImage(LeafDataBuilder builder) {
        if (stateImage == null)
            stateImage = builder.loadImage(getStateResourceId());
        return stateImage;
    }
    protected abstract int getStateResourceId();
}

class LeafTopState extends LeafDataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.leafTop = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {
        builder.setState(new LeafBottomState());
    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_1;
    }
}

class LeafBottomState extends LeafDataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.leafBottom = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {

    }

    @Override
    protected int getStateResourceId() {
        return R.drawable.leaf_2;
    }
}