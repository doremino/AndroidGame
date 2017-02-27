package com.t2t.studio.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by tom on 17/02/01.
 */

public class TitleView extends View {
    private Bitmap titleGraphic;
    private Bitmap playButtonUp;
    private Bitmap playButtonDown;
    private int screenW;
    private int screenH;
    private boolean playButtonPressed = false;
    private Context myContext;

    /**
     *
     * @param context
     */
    public TitleView(Context context) {
        super(context);
        myContext = context;
        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
        playButtonUp = BitmapFactory.decodeResource(getResources(),R.drawable.button_play);
        playButtonDown = BitmapFactory.decodeResource(getResources(),R.drawable.button_play_press);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(titleGraphic, (screenW-titleGraphic.getWidth())/2, (screenH
        - titleGraphic.getHeight())/2, null);
        if (playButtonPressed) {
            canvas.drawBitmap(playButtonDown,(screenW - playButtonDown.getWidth()) / 2, (int) (screenH * 0.9), null);
        }else {
            canvas.drawBitmap(playButtonUp, (screenW - playButtonUp.getWidth()) / 2, (int) (screenH * 0.9), null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();
        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                if ((X > (screenW-playButtonUp.getWidth())/2)&& (X < (screenW-playButtonUp.getWidth())/2 + playButtonUp.getWidth())
                        && (Y > (int)(screenH*0.9))
                        && (Y < ((int)(screenH*0.9) + playButtonUp.getHeight()))) {
                    playButtonPressed = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (playButtonPressed) {
                    // send intent to start game activity
                    Intent gameIntent = new Intent(myContext,GameActivity.class);
                    // start game activity
                    myContext.startActivity(gameIntent);
                }
                 playButtonPressed = false;
                break;
        }
        // trigger for refresh display
        invalidate();
        return true;
    }
}
