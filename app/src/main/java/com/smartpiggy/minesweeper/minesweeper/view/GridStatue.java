package com.smartpiggy.minesweeper.minesweeper.view;

/**
 * Created by eyang on 12/20/15.
 */

//This interface represents grid's different states
public interface GridStatue {
     boolean setMine();
     void reveal();
     void revealTwice();
     void markMine();
}
