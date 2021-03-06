package com.smartpiggy.minesweeper.minesweeper.view;

import com.smartpiggy.minesweeper.minesweeper.R;
import com.smartpiggy.minesweeper.minesweeper.Util.Utils;

/**
 * Created by eyang on 12/20/15.
 */
public class MineStatue implements GridStatue {
    private GridView mGrid;

    public MineStatue(GridView grid){
        this.mGrid = grid;
    }

    @Override
    public boolean setMine() {
        return false;
    }

    //Mine state's grid, reveal the grid will make the game ends
    @Override
    public void reveal() {
        mGrid.currentState = mGrid.revealedState;
        mGrid.setBackground(Utils.getDrawable(mGrid.getContext(), R.mipmap.mine));
        Board.getInstance().cheatAll();
        Board.getInstance().setGameEnd();
        Utils.showDialog(mGrid.getContext(), mGrid.getContext().getString(R.string.game_fail));
    }

    @Override
    public void revealTwice() {
        //not able to revealTwice
    }

    @Override
    public void markMine() {
        mGrid.markedState.setHasMine(true); //set the markedState to have mine
        mGrid.currentState = mGrid.markedState;//change to markedState
        mGrid.setBackground(Utils.getDrawable(mGrid.getContext(), R.mipmap.flag));
    }
}
