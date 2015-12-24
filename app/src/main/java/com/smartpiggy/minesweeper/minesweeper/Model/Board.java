package com.smartpiggy.minesweeper.minesweeper.Model;

import android.util.Log;

import com.smartpiggy.minesweeper.minesweeper.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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
            if (row>=0 && row<mSize && col>=0 && col<mSize) {
                list.add(mGrids[row][col]);
            }
        }
        return list;
    }

    //using BFS(Breadth-first search) to reveal the grids one by one
    public void revealAround(int row, int col){
        //make sure a grid is revealed more than once
        //Set store Integer corresponding to row*mSize+col
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<Integer>();

        for (Grid grid : getAroundGrids(row, col)){
            int index = grid.mRow*mSize + grid.mCol;
            queue.add(index);
            visited.add(index);
        }
        while (!queue.isEmpty()){
            int index = queue.poll();
            int x = index/mSize, y = index%mSize;
            Grid grid = mGrids[x][y];//get grid based on index
            if (grid.currentState==grid.unRevealState){
                if (grid.mNum>0) {
                    grid.reveal();//reveal the number of the grid
                } else{ //mNum equals 0, put the around grids' index to queue
                    grid.currentState = grid.revealedState;//change grid status
                    grid.setBackgroundResource(R.drawable.white_grid);//change grid color to white
                    for (Grid tmpGrid : getAroundGrids(x, y)){
                        int tmpIndex = tmpGrid.mRow*mSize + tmpGrid.mCol;
                        if (visited.contains(tmpIndex)) {//remove duplicate grids
                            continue;
                        }
                        queue.add(tmpIndex);//put around grids to queue
                        visited.add(tmpIndex);
                    }
                }
            }else if (grid.currentState==grid.mineState){
                visited.clear();
                queue.clear();
                grid.reveal();//reveal a mine, game end
                return;
            }
        }
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
}
