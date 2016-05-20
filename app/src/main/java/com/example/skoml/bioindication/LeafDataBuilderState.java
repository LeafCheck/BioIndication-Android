package com.example.skoml.bioindication;

import android.graphics.Point;

/**
 * Created by Developer on 5/20/2016.
 */
public abstract class LeafDataBuilderState {
    public abstract void setPoint(LeafDataBuilder builder, Point point);
    public abstract void nextStep(LeafDataBuilder builder);
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
}

class LeafBottomState extends LeafDataBuilderState {

    @Override
    public void setPoint(LeafDataBuilder builder, Point point) {
        builder.leafBottom = point;
    }

    @Override
    public void nextStep(LeafDataBuilder builder) {

    }
}