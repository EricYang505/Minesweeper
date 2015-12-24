package com.smartpiggy.minesweeper.minesweeper.Model;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.smartpiggy.minesweeper.minesweeper.R;
import com.smartpiggy.minesweeper.minesweeper.Utils;

//Represent each grid in the game board
public class Grid extends TextView {

    int mRow;
    int mCol;
    //four different states for a grid
    MarkedState markedState = new MarkedState(this); //grid has been marked
    MineState mineState = new MineState(this); //grid has a mine
    RevealedState revealedState = new RevealedState(this); //grid has been revealed
    UnRevealState unRevealState = new UnRevealState(this); //grid is not revealed and also do not has a mine
    GridState currentState; //the current state of the grid

    int mNum = 0; //number of mines around

    public Grid(Context context, int row, int col, int viewSize) {
        super(context);
        mRow = row;
        mCol = col;
        setGravity(Gravity.CENTER);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, viewSize * 0.2f);//set text size based on grid size
        setTextColor(Utils.getColor(context, android.R.color.black));
        setBackgroundResource(R.drawable.grey_grid);
        currentState = unRevealState; //default state is unRevealState
    }

    public boolean setMine(){
        return currentState.setMine();
    }

    public boolean getIsRevealed(){
        if (currentState==revealedState) //revealedState are both as revealed
            return true;
        return false;
    }

    //determine if the grid has mine
    public boolean getIsMine(){
        if (currentState == mineState || (currentState == markedState && markedState.isHasMine())) {
            return true;
        }
        return false;
    }

    public void reveal(){
        currentState.reveal();
    }

    public void revealTwice(){//reveal around for the grid which has mNum>0
        currentState.revealTwice();
    }

    public void markMine(){//mark or unMark
        currentState.markMine();
    }

    //show all the mines without end the game
    public void cheat() {
        if (currentState == mineState || (currentState == markedState && markedState.isHasMine())) {
            setBackground(Utils.getDrawable(getContext(), R.mipmap.mine));
        }
    }

}
