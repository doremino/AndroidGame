package com.t2t.studio.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by tom on 17/02/05.
 */

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();
    private Context myContext;
    private List<Card> deck = new ArrayList<Card>();
    private List<Card> myHand = new ArrayList<Card>();
    private List<Card> oppHand = new ArrayList<Card>();
    private List<Card> discardPile = new ArrayList<Card>();
    private int scaledCardW;
    private int scaledCardH;
    private int screenW;
    private int screenH;
    private float scale;
    private Paint whitePaint;
    private int oppScore;
    private int myScore;
    private Bitmap cardBack; // this use for the card of the computer
    private Bitmap nextCardButton;
    private boolean myTurn;
    // for moving cards
    private int movingCardIdx = -1;
    private int movingX;
    private int movingY;
    private int validRank = 8;
    private int validSuit = 0;

    /**
     * @param context
     */
    public GameView(Context context) {
        super(context);
        myContext = context;
        scale = myContext.getResources().getDisplayMetrics().density;
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setTextAlign(Paint.Align.LEFT);
        whitePaint.setTextSize(scale * 15);
        myTurn = new Random().nextBoolean();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        //canvas.drawBitmap(deck.get(0).getBitmap(), 0, 0, null);
        canvas.drawColor(Color.BLACK); // draw black background
        canvas.drawText("Computer Score: " +
                        Integer.toString(oppScore), 10,
                whitePaint.getTextSize() + 10, whitePaint);
        canvas.drawText("My Score: " + Integer.toString(myScore), 10,
                screenH - whitePaint.getTextSize() - 10, whitePaint);

        //draw the back of the opponent cards
        for (int i = 0; i < oppHand.size(); i++) {
            canvas.drawBitmap(cardBack, i * (scale * 5),
                    whitePaint.getTextSize() + (50 * scale), null);
        }
        //draw the draw piles
        canvas.drawBitmap(cardBack, (screenW / 2) - cardBack.getWidth() - 10,
                (screenH / 2) - (cardBack.getHeight() / 2) - (50 * scale), null);
        //draw discard piles
        if (!discardPile.isEmpty()) {
            canvas.drawBitmap(discardPile.get(0).getBitmap(), (screenW / 2) + 10,
                    (screenH / 2) - (cardBack.getHeight() / 2) - (50 * scale), null);
        }
        // draw next card button if the num of card in my hand > 7
        if (myHand.size() > 7) {
            canvas.drawBitmap(nextCardButton,
                    screenW - nextCardButton.getWidth() - (30 * scale),
                    screenH - nextCardButton.getHeight() - scaledCardH/2 - whitePaint.getTextSize() - 10 - (25*scale), null);
        }
        //draw the cards in myHand
        for (int i = 0; i < myHand.size(); i++) {
            if (i == movingCardIdx) {
                canvas.drawBitmap(myHand.get(i).getBitmap(), movingX, movingY, null);
            } else {
                if (i < 7) {
                    canvas.drawBitmap(myHand.get(i).getBitmap(),
                            i * (scaledCardW + 5),
                            screenH - scaledCardH - whitePaint.getTextSize() - (50 * scale), null);
                }
            }
        }
    }

    /**
     * @return
     */
    private boolean checkForValidDraw() {
        boolean canDraw = true;
        for (int i = 0; i < myHand.size(); i++) {
            int tempId = myHand.get(i).getId();
            int tempRank = myHand.get(i).getRank();
            int tempSuit = myHand.get(i).getSuilt();
            if (validSuit == tempSuit ||
                    validRank == tempRank ||
                    tempId == 108 || tempId == 208 ||
                    tempId == 308 || tempId == 408) {
                canDraw = false;
            }
        }

        return canDraw;
    }

    /**
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        screenH = h;
        screenW = w;
        scaledCardW = (int) (screenW / 8);
        scaledCardH = (int) (scaledCardW * 1.28);

        Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(),
                R.drawable.card_back);
        cardBack = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false);
        // init all cards
        initCards();
        dealCards();
        drawCard(discardPile);
        validSuit = discardPile.get(0).getSuilt();
        validRank = discardPile.get(0).getRank();
        // add next card button
        nextCardButton = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_next);
    }

    /**
     * initialize all 52 cards in table
     */
    private void initCards() {
        // loop through 4 groups of cards
        for (int iGroup = 0; iGroup < 4; iGroup++) {
            // iterate through 13 type of one Card Group
            for (int iCardIdx = 102; iCardIdx < 115; iCardIdx++) {
                int iCardId = iCardIdx + iGroup * 100;
                // create new Card object
                Card tempCard = new Card(iCardId);
                //get drawable resourceId
                int resourceId = getResources().getIdentifier("card" + iCardId, "drawable", myContext.getPackageName());
                // decode bitmap resource
                Bitmap tempBitmap = BitmapFactory.decodeResource(myContext.getResources(), resourceId);
//                scaledCardW = (int) (screenW / 8);
//                scaledCardH = (int) (scaledCardW * 1.28);
                // create new scaled bitmap
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, scaledCardW, scaledCardH, false);
                //add bitmap to card
                tempCard.setBitmap(scaledBitmap);
                // add new card to deck
                deck.add(tempCard);
            }
        }
    }

    /**
     * draw one card from deck. This is first card in deck
     *
     * @param handToDraw
     */
    private void drawCard(List<Card> handToDraw) {
        handToDraw.add(0, deck.get(0));
        deck.remove(0);
        // when deck is empty, the card in discardPile will be rolled
        // back to deck
        if (deck.isEmpty()) {
            for (int i = discardPile.size() - 1; i > 0; i--) {
                deck.add(discardPile.get(i));
                discardPile.remove(i);
                Collections.shuffle(deck, new Random());
            }
        }
    }

    /**
     * deliver cards to two player
     */
    private void dealCards() {
        Collections.shuffle(deck, new Random());
        for (int i = 0; i <= 7; i++) {
            drawCard(myHand);
            drawCard(oppHand);
        }
    }

    /**
     *
     */
    private void showChooseSuitDialog() {
        final Dialog chooseSuitDialog = new Dialog(myContext);
        chooseSuitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set layout view for dialog
        chooseSuitDialog.setContentView(R.layout.choose_suit_dialog);
        // obtain spinner for next process
        final Spinner suitSpinner = (Spinner) chooseSuitDialog.findViewById(R.id.suitSpinner);
        // prepare ArrayAdapter for spinner display text
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        myContext, R.array.suits, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // assign adapter to spinner
        suitSpinner.setAdapter(adapter);
        // processing for Button
        Button okButton = (Button) chooseSuitDialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // choose item from spinner
                validSuit = (suitSpinner.getSelectedItemPosition() + 1) * 100;
                String suitText = "";
                if (validSuit == 100) {
                    suitText = "Diamonds";
                } else if (validSuit == 200) {
                    suitText = "Clubs";
                } else if (validSuit == 300) {
                    suitText = "Hearts";
                } else if (validSuit == 400) {
                    suitText = "Spades";
                }
                chooseSuitDialog.dismiss();
                Toast.makeText(myContext,
                        "You chose " + suitText, Toast.LENGTH_SHORT).show();
            }
        });
        chooseSuitDialog.show();
    }

    /**
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                if (myTurn) {
                    for (int i = 0; i < 7; i++) {
                        if (X > (i * (scaledCardW + 5)) &&
                                X < (scaledCardW + (i * (scaledCardW + 5)))
                                && Y > (screenH - scaledCardH - whitePaint.getTextSize() - 50 * scale)
                                && Y < (screenH - whitePaint.getTextSize() - 50 * scale)) {
                            movingCardIdx = i;
                            movingX = X - (int) (30 * scale);
                            movingY = Y - (int) (70 * scale);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                movingX = X - (int) (30 * scale);
                movingY = Y - (int) (70 * scale);
                break;
            case MotionEvent.ACTION_UP:
                if ((movingCardIdx > -1) &&
                        X > ((screenW / 2) - (100 * scale)) &&
                        X < ((screenW / 2) + (100 * scale)) &&
                        Y > ((screenH / 2) - (100 * scale)) &&
                        Y < ((screenH / 2) + (100 * scale)) &&
                        ((myHand.get(movingCardIdx).getRank() == 8) ||
                                (myHand.get(movingCardIdx).getRank() == validRank) ||
                                (myHand.get(movingCardIdx).getSuilt() == validSuit))) {
                    validRank = myHand.get(movingCardIdx).getRank();
                    validSuit = myHand.get(movingCardIdx).getSuilt();
                    discardPile.add(0, myHand.get(movingCardIdx));
                    myHand.remove(movingCardIdx);
                    if (validRank == 8) {
                        showChooseSuitDialog();
                    }
                }
                movingCardIdx = -1;
                if (movingCardIdx == -1 && myTurn &&
                        X > ((screenW / 2) - (100 * scale)) &&
                        X < ((screenW / 2) + (100 * scale)) &&
                        Y > ((screenH / 2) - (100 * scale)) &&
                        Y < ((screenH / 2) + (100 * scale))) {
                     if ( checkForValidDraw()) {
                         drawCard(myHand);
                     } else {
                         Toast.makeText(myContext,"You have valid play.",Toast.LENGTH_SHORT).show();
                     }
                }
                break;
        }
        invalidate();
        return true;
    }
}
