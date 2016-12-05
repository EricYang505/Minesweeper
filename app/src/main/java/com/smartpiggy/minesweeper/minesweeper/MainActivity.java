package com.smartpiggy.minesweeper.minesweeper;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.smartpiggy.minesweeper.minesweeper.view.Board;
import com.smartpiggy.minesweeper.minesweeper.view.GridView;
import com.smartpiggy.minesweeper.minesweeper.Util.Utils;

//How to play:
//        1. The game by default has 8*8 grids and 10 mines. User can change difficulty level by size and mine number and hit New Game. To cheat the game, click the cheat button to reveal all the mines but the game will continue.
//        Reveal a mine, and the game ends.
//
//        2. Reveal an empty square, and you keep playing.
//
//        3. Reveal a number, and it tells you how many mines lay hidden in the eight surrounding squares
//
//        4. If user suspect a square conceals a mine, long click on the grid to mark a mine, a red flag will show(long click again will unmark the grid). When user mark all the mines around an unmarked grid which shows a number, then can click on the number grid to reveal all the around grids which haven’t marked.
//        When user reveal all the grids which do not have mines, click the verify button to check if you finish the game successfully.

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String[] mDifficultySize;
    private String[] mDifficultyMine;
    private LinearLayout mBroadLayout;
    private Spinner mSizeSpinner;
    private Spinner mMineSpinner;
    private Button mValidateBtn;
    private Button mNewGameBtn;
    private Button mCheatBtn;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        resetAndInitBoard();
        initAds();
    }

    private void initAds(){
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("CAE9ADD7A5C5751A095EB468C8829208")
                .build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroy board because it references the gridView which may cause memory leak
        Board.getInstance().destroyBoard();
    }


    private void initView(){
        mBroadLayout = (LinearLayout) findViewById(R.id.boardId);
        mNewGameBtn = (Button)findViewById(R.id.newGameId);
        mNewGameBtn.setOnClickListener(this);
        mValidateBtn = (Button)findViewById(R.id.validateId);
        mValidateBtn.setOnClickListener(this);
        mCheatBtn = (Button)findViewById(R.id.cheateId);
        mCheatBtn.setOnClickListener(this);
        mSizeSpinner = (Spinner) findViewById(R.id.spinnerSizeId);
        mDifficultySize = getResources().getStringArray(R.array.difficulty_level_size);
        ArrayAdapter dataAdapterSize = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mDifficultySize);
        dataAdapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(dataAdapterSize);
        mSizeSpinner.setSelection(0);
        mMineSpinner = (Spinner) findViewById(R.id.spinnerMineId);
        mDifficultyMine = getResources().getStringArray(R.array.difficulty_level_mine);
        ArrayAdapter dataAdapterMine = new ArrayAdapter(this,android.R.layout.simple_spinner_item, mDifficultyMine);
        dataAdapterMine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMineSpinner.setAdapter(dataAdapterMine);
        mMineSpinner.setSelection(0);
    }

    //Fill in grids to the game board based on difficulty
    private void resetAndInitBoard(){
        int currentSize = Integer.valueOf(mSizeSpinner.getSelectedItem().toString());
        int currentMineNum = Integer.valueOf(mMineSpinner.getSelectedItem().toString());
        if (currentMineNum>=currentSize*currentSize){
            Toast.makeText(this, getString(R.string.mine_number_too_big), Toast.LENGTH_SHORT).show();
            return;
        }
        Board.getInstance().init(currentSize, currentMineNum);//init board data
        mBroadLayout.removeAllViews();
        Display display = getWindowManager().getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        //get gridSize based on screen size
        int gridSize = (Math.min(screenSize.x, screenSize.y) / (currentSize + 1))-2;

        //generate all grids based on LinearLayout
        for (int i = 0; i < currentSize; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < currentSize; j++) {
                GridView grid = new GridView(this, i, j, gridSize);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridSize, gridSize);
                Board.getInstance().setGrid(i, j, grid);
                linearLayout.addView(grid, params);
                grid.setOnClickListener(gridClickListener);//handle click event
                grid.setOnLongClickListener(gridLongPressListener);//handle long click event
            }
            mBroadLayout.addView(linearLayout);
        }
        Board.getInstance().generateMine();
    }

    private View.OnClickListener gridClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Board.getInstance().getIsGameEnd())
                return;
            if (v instanceof GridView) {
                GridView grid = (GridView) v;
                if (grid.getIsRevealed())
                    grid.revealTwice();//when mark all the mines around, able to help reveal the grids around
                else
                    grid.reveal();
            }
        }
    };

    private View.OnLongClickListener gridLongPressListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {
            if (Board.getInstance().getIsGameEnd())
                return true;
            if (v instanceof GridView) {
                GridView grid = (GridView) v;
                grid.markMine();
            }
            return true;
        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.newGameId){
            resetAndInitBoard();
        }else if(v.getId()==R.id.cheateId){
            Board.getInstance().cheatAll();
        }else if (v.getId()==R.id.validateId){
            if (Board.getInstance().validateGame()){
                Utils.showDialog(this, getString(R.string.game_success));
            }else {
                Utils.showDialog(this, getString(R.string.game_reveal_fail));
            }
        }
    }
}
