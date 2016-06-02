package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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
    private boolean isPanMode = false;

    public LinesDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimension(R.dimen.path_width));
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
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

        builder.drawState(canvas);
        if (activeCursor != null)
            drawPointer(canvas);
    }

    private void drawPointer(Canvas canvas) {
        float size = getResources().getDimension(R.dimen.pointer_size);
        canvas.drawLine(activeCursor.x - size, activeCursor.y, activeCursor.x + size, activeCursor.y, paint);
        canvas.drawLine(activeCursor.x, activeCursor.y - size, activeCursor.x, activeCursor.y + size, paint);
    }

    private long tapTimer;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        if (action == MotionEvent.ACTION_DOWN) {
            possibleCursor = new Point((int) event.getX(), (int) event.getY());
            tapTimer = System.currentTimeMillis();
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            isPanMode = true;
        } else if (action == MotionEvent.ACTION_POINTER_UP) {
            isPanMode = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            int deltaX = ((int) event.getX() - lastPoint.x);
            int deltaY = ((int) event.getY() - lastPoint.y);
            if (isPanMode) {
                builder.panLeafImage(deltaX, deltaY);
            } else {
                if ((activeCursor != null)) {
                    activeCursor.x = activeCursor.x + (deltaX / 2);
                    activeCursor.y = activeCursor.y + (deltaY / 2);
                    hasMoved = true;
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {
            if (!hasMoved || ((System.currentTimeMillis() - tapTimer) < 100)) {
                activeCursor = possibleCursor;
            }
            hasMoved = false;
        }

        if (action != MotionEvent.ACTION_DOWN)
            activeCursor = builder.adjustPointerPosition(activeCursor);
        lastPoint = new Point((int) event.getX(), (int) event.getY());
        invalidate();
        super.onTouchEvent(event);
        return true;
    }

    public boolean nextStep() {
        if (activeCursor != null) {
            if (!builder.processPoint(activeCursor)) {
                // finish calculation flow
                Toast.makeText(LinesDrawer.this.getContext(), "Calculation result: " + leaf.getValue(), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        activeCursor = null;
        possibleCursor = null;
        hasMoved = false;
        invalidate();
        return true;
    }
}
