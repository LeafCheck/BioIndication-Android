package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by Developer on 5/20/2016.
 */
public class LeafDataBuilder {
    public LeafData leafData = null;
    private LeafDataBuilderState state = null;
    private Context context = null;

    // key points
    public Point leafTop = null;
    public Point leafBottom = null;

    public LeafDataBuilder(LeafData leafData, Context context) {
        this.leafData = leafData;
        this.context = context;
        setState(new LeafTopState());
    }

    public void setState(LeafDataBuilderState state) {
        this.state = state;
    }

    public void processPoint(Point point) {
        state.setPoint(this, point);
        state.nextStep(this);
    }

    public Bitmap getStateImage() {
        return state.getStateImage(this);
    }

    public Bitmap loadImage(int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }
}
