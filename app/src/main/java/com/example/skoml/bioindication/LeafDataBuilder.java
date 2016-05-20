package com.example.skoml.bioindication;

import android.graphics.Point;

/**
 * Created by Developer on 5/20/2016.
 */
public class LeafDataBuilder {
    public LeafData leafData = null;
    private LeafDataBuilderState state = null;

    // key points
    public Point leafTop = null;
    public Point leafBottom = null;

    public LeafDataBuilder(LeafData leafData) {
        this.leafData = leafData;
        setState(new LeafTopState());
    }

    public void setState(LeafDataBuilderState state) {
        this.state = state;
    }

    public void processPoint(Point point) {
        state.setPoint(this, point);
        state.nextStep(this);
    }
}
