package com.smartpiggy.minesweeper.minesweeper.view;

import android.widget.Toast;

import com.smartpiggy.minesweeper.minesweeper.R;
import com.smartpiggy.minesweeper.minesweeper.Util.Utils;

/**
 * Created by eyang on 12/20/15.
 */
public class RevealedStatue implements GridStatue {
    private GridView mGrid;

    public RevealedStatue(GridView grid){
        this.mGrid = grid;
    }

    @Override
    public boolean setMine() {
        return false;
    }

    @Override
    public void reveal() {
        //already in reveal state
    }

    @Override
    public void revealTwice() {
        if (mGrid.mNum>0){ //has mines around
            int markCount = 0;
            for (GridView grid : Board.getInstance().getAroundGrids(mGrid.mRow, mGrid.mCol)){
                if (grid.currentState==grid.markedState) {
                    markCount++;
                }
            }
            if (markCount>mGrid.mNum){//mark mines number is bigger than actually number, game ends
                Board.getInstance().cheatAll();
                Board.getInstance().setGameEnd();
                Utils.showDialog(mGrid.getContext(), mGrid.getContext().getString(R.string.game_fail));
            }else if (markCount==mGrid.mNum){//mark all the mines around, able to help reveal around grids
                Board.getInstance().revealAround(mGrid.mRow, mGrid.mCol);
            }else {
                Toast.makeText(mGrid.getContext(), mGrid.getContext().getString(R.string.mark_more), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void markMine() {
        //revealed grid not able to mark
    }
}
