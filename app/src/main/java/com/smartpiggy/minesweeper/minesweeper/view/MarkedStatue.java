package com.smartpiggy.minesweeper.minesweeper.view;

import com.smartpiggy.minesweeper.minesweeper.R;

/**
 * Created by eyang on 12/20/15.
 */
public class MarkedStatue implements GridStatue {
    private GridView mGrid;
    private boolean mHasMine = false;

    public MarkedStatue(GridView grid){
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
            mGrid.currentState = mGrid.mineState;//change to mine state
        }else {
            mGrid.currentState = mGrid.unRevealState;//change to unRevealed state
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
