package com.smartpiggy.minesweeper.minesweeper.Model;

import com.smartpiggy.minesweeper.minesweeper.R;
import com.smartpiggy.minesweeper.minesweeper.Utils;

/**
 * Created by eyang on 12/20/15.
 */
public class UnRevealState implements GridState{
    private Grid mGrid;

    public UnRevealState(Grid grid){
        this.mGrid = grid;
    }

    @Override
    public boolean setMine() {
        mGrid.currentState = mGrid.mineState;
        return true;
    }

    @Override
    public void reveal() {
        mGrid.currentState = mGrid.revealedState;
        if (mGrid.mNum == 0) {//reveal the grids around
            mGrid.setBackgroundResource(R.drawable.white_grid);
            Board.getInstance().revealAround(mGrid.mRow, mGrid.mCol);
        }else{//reveal the number of mines around
            mGrid.setText(String.valueOf(mGrid.mNum));
        }
    }

    @Override
    public void revealTwice() {
        //not able to revealTwice
    }

    @Override
    public void markMine() {
        mGrid.markedState.setHasMine(false);
        mGrid.currentState = mGrid.markedState;
        mGrid.setBackground(Utils.getDrawable(mGrid.getContext(), R.mipmap.flag));
    }
}
