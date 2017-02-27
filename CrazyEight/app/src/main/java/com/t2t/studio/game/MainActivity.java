package com.t2t.studio.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        CrazyEightView myView = new CrazyEightView(this);
//        // set our view to display
//        setContentView(myView);
        TitleView titleView = new TitleView(this);
        //GameView gameView = new GameView(this);
        // keep screen always turn on
        titleView.setKeepScreenOn(true);
        //gameView.setKeepScreenOn(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // config the image full of screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(titleView);
        //setContentView(gameView);
    }
}
