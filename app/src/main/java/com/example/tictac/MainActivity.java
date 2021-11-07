package com.example.tictac;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    //Player X = 1, true
    //Player O = 2, false
    //empty = 0
    Boolean turnFlag, gameModeFlag;
    int[][] board = new int[3][3];
    ImageButton[] boardBtns = new ImageButton[9];
    Button restartBtn;
    ImageView main_player, main_line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_player = findViewById(R.id.main_player);
        restartBtn = findViewById(R.id.main_restart_btn);
        main_line = findViewById(R.id.main_win_line);
        initBtns();
        restartGame();
    }

    private void initBtns(){
        boardBtns[0] = findViewById(R.id.main_button_1);
        boardBtns[1] = findViewById(R.id.main_button_2);
        boardBtns[2] = findViewById(R.id.main_button_3);
        boardBtns[3] = findViewById(R.id.main_button_4);
        boardBtns[4] = findViewById(R.id.main_button_5);
        boardBtns[5] = findViewById(R.id.main_button_6);
        boardBtns[6] = findViewById(R.id.main_button_7);
        boardBtns[7] = findViewById(R.id.main_button_8);
        boardBtns[8] = findViewById(R.id.main_button_9);
        restartBtn.setOnClickListener(v-> restartGame());
        boardBtns[0].setOnClickListener(v -> turn(0,0));
        boardBtns[1].setOnClickListener(v -> turn(0,1));
        boardBtns[2].setOnClickListener(v -> turn(0,2));
        boardBtns[3].setOnClickListener(v -> turn(1,0));
        boardBtns[4].setOnClickListener(v -> turn(1,1));
        boardBtns[5].setOnClickListener(v -> turn(1,2));
        boardBtns[6].setOnClickListener(v -> turn(2,0));
        boardBtns[7].setOnClickListener(v -> turn(2,1));
        boardBtns[8].setOnClickListener(v -> turn(2,2));
        main_line = findViewById(R.id.main_win_line);
    }
    public void restartGame(){
        turnFlag = true;
        gameModeFlag = true;
        main_player.setImageResource(R.drawable.xplay);
        main_line.setImageResource(R.drawable.empty);
        Arrays.fill(board[0],0);
        Arrays.fill(board[1],0);
        Arrays.fill(board[2],0);
        for(int i = 0; i < 9; i++)
            boardBtns[i].setBackgroundResource(R.drawable.empty);
    }
    public int[] getCol(int colIndex){
        return new int[]{board[0][colIndex],board[1][colIndex],board[2][colIndex]};
    }
    public void turn(int row,int col){
        if(board[row][col] != 0 || !gameModeFlag)
            return;
        if(turnFlag){
            board[row][col] = 1;
            boardBtns[row*3 + col].setBackgroundResource(R.drawable.x);
            main_player.setImageResource(R.drawable.oplay);
        }
        else{
            board[row][col] = 2;
            boardBtns[row*3 + col].setBackgroundResource(R.drawable.o);
            main_player.setImageResource(R.drawable.xplay);
        }
        turnFlag = !turnFlag;
        if(checkWins() != 0)
            endGame(checkWins());
    }

    public void draw_win_line(int i, int j){
        switch(i + j*4){
            case 0:
                main_line.setImageResource(R.drawable.mark6);
                break;
            case 1:
                main_line.setImageResource(R.drawable.mark7);
                break;
            case 2:
                main_line.setImageResource(R.drawable.mark8);
                break;
            case 3:
                main_line.setImageResource(R.drawable.mark1);
                break;
            case 4:
                main_line.setImageResource(R.drawable.mark3);
                break;
            case 5:
                main_line.setImageResource(R.drawable.mark4);
                break;
            case 6:
                main_line.setImageResource(R.drawable.mark5);
                break;
            case 7:
                main_line.setImageResource(R.drawable.mark2);
                break;
            default:
                break;
        }
    }
    public void endGame(int c){
        if(c == 1)
            main_player.setImageResource(R.drawable.xwin);
        if(c == 2)
            main_player.setImageResource(R.drawable.owin);
        if(c == -1)
            main_player.setImageResource(R.drawable.nowin);
        gameModeFlag = false;
    }
    public int checkPath(int[] vec){
        if(vec[0] == vec[1] && vec[0] == vec[2] && vec[0] != 0) {
            if (vec[0] == 1)
                return 1;
            else
                return 2;
        }
        if(vec[0] == 0 || vec[1] == 0 || vec[2] == 0)
            return 0;
        return -1;
    }
    public int checkWins(){
        //-1 - no wins and no more moves.
        //0 - no wins and there are moves.
        //1 - X wins.
        //2 - O wins.
        //Checks 2 variants each loop.
        int i, j;
        int[] result = new int[2]; //[0] - row, [1] - col
        Boolean moves = false;
        for(i = 0; i < 4; i++) {
            //Checks 2 variants each loop.
            //i = (0, 2) checks row and column each time
            //i = 3 checks diagonals.
            if(i == 3){
                result[0] = checkPath(new int[]{board[0][0], board[1][1], board[2][2]});
                result[1] = checkPath(new int[]{board[0][2], board[1][1], board[2][0]});
            }
            else{
                result[0] = checkPath(board[i]);         //row i
                result[1] = checkPath(getCol(i));        //col i
            }
            for(j =0;j<2;j++) {
                switch (result[j]) {
                    case 0://there are more moves on this path.
                        moves = true;
                        break;
                    case 1:
                        draw_win_line(i, j);
                        return 1;
                    case 2://O wins
                        draw_win_line(i, j);
                        return 2;
                    default:// no more moves
                        break;
                }
            }
        }
        if(!moves)
            return -1;
        return 0;
    }
}