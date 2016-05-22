package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ecometr.app.R;

/**
 * Created by skoml on 03.05.2016.
 */
public class LinesDrawer extends View {

    private Paint paint;
    private LeafData leaf = null;
    private LeafDataBuilder builder = null;
    private Point activeCursor = null;
    private Point possibleCursor = null;
    private Point lastPoint = null;
    private boolean hasMoved = false;

    public LinesDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimension(R.dimen.path_width));
        paint.setColor(Color.WHITE);
        paint.setAlpha(100);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public LinesDrawer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinesDrawer(Context context) {
        this(context, null, 0);
    }

    public void setLeaf(LeafData aLeaf) {
        leaf = aLeaf;
        builder = new LeafDataBuilder(leaf, getContext());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (leaf != null)
            leaf.Draw(canvas);

        if (activeCursor != null)
            canvas.drawPoint(activeCursor.x, activeCursor.y, paint);

        canvas.drawBitmap(builder.getStateImage(), 10, 10, null);

//        Point middle = builder.getLeafMiddle();
//        if (middle != null) {
//            canvas.drawPoint(middle.x, middle.y, paint);
//            Point right = builder.getLeafSidePoint(-200);
//            canvas.drawLine(middle.x, middle.y, right.x, right.y, paint);
//        }
    }

    private long tapTimer;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            possibleCursor = new Point((int) event.getX(), (int) event.getY());
            tapTimer = System.currentTimeMillis();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int deltaX = ((int) event.getX() - lastPoint.x);
            int deltaY = ((int) event.getY() - lastPoint.y);
            if ((activeCursor != null)) {
                activeCursor.x = activeCursor.x + (deltaX / 2);
                activeCursor.y = activeCursor.y + (deltaY / 2);
                hasMoved = true;
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!hasMoved || ((System.currentTimeMillis() - tapTimer) < 100)) {
                activeCursor = possibleCursor;
            }
            hasMoved = false;
        }

        activeCursor = builder.fixPointPosition(activeCursor);
        lastPoint = new Point((int) event.getX(), (int) event.getY());
        invalidate();
        super.onTouchEvent(event);
        return true;
    }

    public void nextStep() {
        if (activeCursor != null)
            builder.processPoint(activeCursor);

        activeCursor = null;
        possibleCursor = null;
        hasMoved = false;
        invalidate();
    }
}
