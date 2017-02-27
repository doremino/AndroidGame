package com.t2t.studio.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tom on 17/01/30.
 */

public class CrazyEightView extends View {
    private Paint redPaint;
    private int circleX; // this coordinate of the circle
    private int circleY;
    private float radius;

    /**
     *
     * @param context
     */
    public CrazyEightView(Context context) {
        super(context);
        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.RED);
        circleX = 100;
        circleY = 100;
        radius = 30;
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //draw circle
        canvas.drawCircle(circleX,circleY,radius,redPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int event_action = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (event_action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                circleX = X;
                circleY = Y;
                break;
        }
        invalidate(); // this api use for trigger update display
        return true;
    }
}
