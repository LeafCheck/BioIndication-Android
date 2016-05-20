package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ecometr.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skoml on 03.05.2016.
 */
public class LinesDrawer extends View {


    private final List<Point> points = new ArrayList<Point>();

    private boolean roaming = false;

    private Paint paint;

    private LeafData leaf = null;

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
        invalidate();
    }

    Path path = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (leaf != null)
            leaf.Draw(canvas);

        if (activeCursor != null)
            canvas.drawPoint(activeCursor.x, activeCursor.y, paint);

//        path.reset();
//        boolean first = true;
//        final int size = points.size();
//        for (int i = 1; i < size; i += 2) {
//            Point p1 = points.get(i - 1);
//            Point p2 = points.get(i);
//            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
//        }
//        if (size % 2 != 0) {
//            Point p1 = points.get(size - 1);
//            canvas.drawPoint(p1.x, p1.y, paint);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.v(this.getClass().getName(), "TOUCH " + event.toString() + " " + Arrays.deepToString(points.toArray()));

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            possibleCursor = new Point((int) event.getX(), (int) event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int deltaX = ((int) event.getX() - lastPoint.x);
            int deltaY = ((int) event.getY() - lastPoint.y);
            if ((activeCursor != null) && (deltaX < 10) && (deltaY < 10)) {
                activeCursor.x = activeCursor.x + deltaX;
                activeCursor.y = activeCursor.y + deltaY;
            }
            hasMoved = true;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!hasMoved) {
                activeCursor = possibleCursor;
                invalidate();
            }
            hasMoved = false;
        }

        lastPoint = new Point((int) event.getX(), (int) event.getY());
        super.onTouchEvent(event);
        return true;
    }

    public void nextStep() {
    }
}
