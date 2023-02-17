package com.ibrahimsahan.nutritionapp;

import java.util.Date;

public class PlusMinusButtonHelper {

    int pieceSoap = 0;
    int pieceMainCourse = 0;
    int pieceDessert = 0;
    int pieceDrink = 0;
    int pieceBread = 0;

    public int plusPieceSoap() {
        return ++pieceSoap;
    }

    public int minusPieceSoap() {
        if (pieceSoap == 0) {
            return pieceSoap;
        }
            pieceSoap--;
            return pieceSoap;
    }

    public int plusPieceMainCourse() {
        pieceMainCourse++;
        return pieceMainCourse;
    }

    public int minusPieceMainCourse() {
        if (pieceMainCourse == 0) {
            pieceMainCourse = 0;
            return pieceMainCourse;
        } else {
            pieceMainCourse--;
            return pieceMainCourse;
        }
    }

    public int plusPieceDessert() {
        pieceDessert++;
        return pieceDessert;
    }

    public int minusPieceDessert() {
        if (pieceDessert == 0) {
            pieceDessert = 0;
            return pieceDessert;
        } else {
            pieceDessert--;
            return pieceDessert;
        }
    }

    public int plusPieceDrink() {
        pieceDrink++;
        return pieceDrink;
    }

    public int minusPieceDrink() {
        if (pieceDrink == 0) {
            pieceDrink = 0;
            return pieceDrink;
        } else {
            pieceDrink--;
            return pieceDrink;
        }
    }

    public int plusPieceBread() {
        pieceBread++;
        return pieceBread;
    }

    public int minusPieceBread() {
        if (pieceBread == 0) {
            pieceBread = 0;
            return pieceBread;
        } else {
            pieceBread--;
            return pieceBread;
        }


    }
}
