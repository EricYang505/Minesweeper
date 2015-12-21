package com.smartpiggy.minesweeper.minesweeper.Model;

/**
 * Created by eyang on 12/20/15.
 */

//This interface represents grid's different states
public interface GridState {
    public boolean setMine();
    public void reveal();
    public void revealTwice();
    public void markMine();
}
