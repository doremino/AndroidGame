package com.t2t.studio.game;

import android.graphics.Bitmap;

/**
 * Created by tom on 17/02/05.
 */

public class Card {
    private int id;
    private int suit;
    private int rank;
    private Bitmap bmp;

    /**
     * the card will be determined by the id
     * as offset with base group of Card:
     * Diamonds(baseId=100)
     * Clubs(baseId=200)
     * Hearts(baseId=300)
     * Spades(baseId=400)
     * And the cards are ranked from 12 to 14 offset
     *
     * @param newId
     */
    public Card(int newId) {
        id = newId;
        suit = Math.round((id / 100) * 100);
        rank = id - suit;
    }

    /**
     *
     * @return
     */
    public int getSuilt() {
        return suit;
    }

    /**
     *
     * @return
     */
    public int getRank() {
        return rank;
    }
    /**
     * @param newBitmap
     */
    public void setBitmap(Bitmap newBitmap) {
        bmp = newBitmap;
    }

    /**
     * @return current Bitmap
     */
    public Bitmap getBitmap() {
        return bmp;
    }

    /**
     * @return
     */
    public int getId() {
        return id;
    }

}
