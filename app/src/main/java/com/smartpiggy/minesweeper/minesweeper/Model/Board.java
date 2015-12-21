package com.smartpiggy.minesweeper.minesweeper.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyang on 12/19/15.
 */

//Board is using singleton pattern since there is only one board in a game
public class Board {
    //Using mAroundDiffs to get the 8 grids' position around
    private int[][] mAroundDiffs = {{-1, -1},{0, -1},{1, -1},{-1, 0},{1, 0},{-1, 1},{0, 1},{1, 1}};
    private static Board instance;
    private Grid[][] mGrids;
    private int mSize;
    private int mMineNum;
    private boolean isGameEnd = true;//check game status

    private Board(){}

    public static synchronized Board getInstance(){
        if (instance==null)
            instance = new Board();
        return instance;
    }

    public void init(int size, int mineNum){ //reset and init all values
        isGameEnd = false;
        mSize = size;
        mMineNum = mineNum;
        mGrids = null;
        mGrids = new Grid[size][size];
    }

    public void setGameEnd(){
        isGameEnd = true;
    }

    public boolean getIsGameEnd(){
        return isGameEnd;
    }

    public void generateMine(){//generate mines by mMineNum
        int count = 0;
        int totalGridNumber = mSize*mSize;
        while (count<mMineNum){
            int index = (int) (Math.random()*totalGridNumber); //generate index from 0 to totalGridNumber-1
            if (mGrids[index/mSize][index%mSize].setMine()) {
                plusGridsAround(index/mSize, index%mSize); //every time set a mine, plus the grids' numbers around
                count++;
            }
        }
    }

    private void plusGridsAround(int x, int y){ //when set a Mine at (x,y), then around grids' number plus one
        for (Grid grid : getAroundGrids(x, y))
            grid.mNum++;
    }

    //get all the grids around and return as a grid list
    public List<Grid> getAroundGrids(int x, int y){
        List<Grid> list = new ArrayList<>();
        for (int[] diff : mAroundDiffs){
            int row = x+diff[0];
            int col = y+diff[1];
            if (row>=0 && row<mSize && col>=0 && col<mSize)
                list.add(mGrids[row][col]);
        }
        return list;
    }

    public void setGrid(int row, int col, Grid grid){
        if (row>=0 && row<mSize && col>=0 && col<mSize)
            mGrids[row][col] = grid;
    }

    public boolean validateGame(){
        setGameEnd();
        for (int i=0; i<mSize; i++){
            for (int j=0; j<mSize; j++){
                //make sure all the unrevealed grids are mines
                if(!mGrids[i][j].getIsRevealed() && !mGrids[i][j].getIsMine()){
                    cheatAll();
                    return false;
                }
            }
        }
        return true;
    }

    public void cheatAll(){//reveal all the mines and the game is also continue
        for (int i=0; i<mSize; i++){
            for (int j=0; j<mSize; j++){
                mGrids[i][j].cheat();
            }
        }
    }

    public void revealAround(int row, int col){
        for (Grid grid : getAroundGrids(row, col)){
            grid.reveal();
        }
    }
}
