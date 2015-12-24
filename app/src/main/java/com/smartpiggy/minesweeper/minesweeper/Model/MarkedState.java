package com.smartpiggy.minesweeper.minesweeper.Model;

import com.smartpiggy.minesweeper.minesweeper.R;
import com.smartpiggy.minesweeper.minesweeper.Utils;

/**
 * Created by eyang on 12/20/15.
 */
public class MarkedState implements GridState{
    private Grid mGrid;
    private boolean mHasMine = false;

    public MarkedState(Grid grid){
        this.mGrid = grid;
    }

    @Override
    public boolean setMine() {
        mHasMine = true;
        return true;
    }

    @Override
    public void reveal() {
        //marked state is not able to reveal
    }

    @Override
    public void revealTwice() {
        //not able to revealTwice
    }

    @Override
    public void markMine() {
        if (mHasMine){
            mGrid.currentState = mGrid.mineState;
        }else {
            mGrid.currentState = mGrid.unRevealState;
        }
        mGrid.setBackgroundResource(R.drawable.grey_grid);
    }

    public boolean isHasMine() {
        return mHasMine;
    }

    public void setHasMine(boolean hasMine) {
        this.mHasMine = hasMine;
    }
}
