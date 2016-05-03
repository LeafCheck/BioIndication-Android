package com.example.skoml.bioindication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import com.ecometr.app.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by skoml on 03.05.2016.
 */
public class LinesDrawer extends View {


    private final List<Point> points = new ArrayList<Point>();

    private boolean roaming = false;

    private Paint paint;
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


    Path path = new Path();

    public void reset(){
        path.reset();
        points.clear();
        invalidate();

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        boolean first = true;
        final int size = points.size();
        for (int i = 1; i < size; i += 2) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
        }
        if (size % 2 != 0) {
            Point p1 = points.get(size - 1);
            canvas.drawPoint(p1.x, p1.y, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.v(this.getClass().getName(), "TOUCH " + event.toString() + " " + Arrays.deepToString(points.toArray()));

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            Point point = new Point((int) event.getX(), (int) event.getY());
            if (points.isEmpty()) {
                points.add(point);
            } else {
                points.set(points.size() - 1, point);
            }
            roaming = false;
            invalidate();
        }
        else

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Point p = new Point((int) event.getX(), (int) event.getY());
            if (!roaming) {
                roaming = true;
                points.add(p);
            } else {
                if(points.isEmpty())
                    points.add(p);
                else
                    points.set(points.size() - 1, p);
            }
            invalidate();
        }
        else if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //points.clear();
        }

        super.onTouchEvent(event);
        return true;
    }

}
