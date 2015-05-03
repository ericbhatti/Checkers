package com.bezyapps.checkers;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    public int selected_row = - 1;
    public int seleted_col = - 1;
    public boolean win;
    public int turn;
    private void initBoxes() {
        int[][] ids = {{R.id.box00, R.id.box01, R.id.box02, R.id.box03, R.id.box04, R.id.box05, R.id.box06, R.id.box07},
                {R.id.box10, R.id.box11, R.id.box12, R.id.box13, R.id.box14, R.id.box15, R.id.box16, R.id.box17},
                {R.id.box20, R.id.box21, R.id.box22, R.id.box23, R.id.box24, R.id.box25, R.id.box26, R.id.box27},
                {R.id.box30, R.id.box31, R.id.box32, R.id.box33, R.id.box34, R.id.box35, R.id.box36, R.id.box37},
                {R.id.box40, R.id.box41, R.id.box42, R.id.box43, R.id.box44, R.id.box45, R.id.box46, R.id.box47},
                {R.id.box50, R.id.box51, R.id.box52, R.id.box53, R.id.box54, R.id.box55, R.id.box56, R.id.box57},
                {R.id.box60, R.id.box61, R.id.box62, R.id.box63, R.id.box64, R.id.box65, R.id.box66, R.id.box67},
                {R.id.box70, R.id.box71, R.id.box72, R.id.box73, R.id.box74, R.id.box75, R.id.box76, R.id.box77}};

        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for (int j = 0; j < ROW_COL_COUNT; j++) {
                boxes[i][j] = (ImageView) findViewById(ids[i][j]);
                boxes[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            int row = Integer.parseInt(v.getTag().toString().substring(0, 1));
                            int col = Integer.parseInt(v.getTag().toString().substring(1, 2));
                            if(selected_row != -1 && seleted_col != -1)
                            {
                                if(CheckersBoard[row][col] == 2)
                                {
                                    boxes[selected_row][seleted_col].setImageDrawable(getResources().getDrawable(R.drawable.you1));
                                    boxes[row][col].setImageDrawable(getResources().getDrawable(R.drawable.you2));
                                    seleted_col = col;
                                    selected_row = row;
                                }
                                if(CheckersBoard[row][col] == 4)
                                {
                                    boxes[selected_row][seleted_col].setImageDrawable(getResources().getDrawable(R.drawable.you1k));
                                    boxes[row][col].setImageDrawable(getResources().getDrawable(R.drawable.you2k));
                                    seleted_col = col;
                                    selected_row = row;
                                }
                                if(CheckersBoard[row][col] == 0)
                                {
                                    ArrayList<int[]> legal = GenerateMoves(2,CheckersBoard);
                                    boolean match = false;
                                    for(int i = 0; i < legal.size(); i++)
                                    {
                                        if(legal.get(i)[0] == selected_row && legal.get(i)[1] == seleted_col && legal.get(i)[2] == row && legal.get(i)[3] == col)
                                        {
                                            match=true;
                                            break;
                                        }
                                    }
                                    if(match)
                                    {
                                        CheckersBoard[row][col] = CheckersBoard[selected_row][seleted_col];
                                        CheckersBoard[selected_row][seleted_col] = 0;
                                        if (Math.abs(row - selected_row) == 2
                                                && Math.abs(col - seleted_col) == 2 ) {
                                            int flag_row = 0, flag_col = 0;
                                            if (row > selected_row) {
                                                flag_row = row - 1;
                                            } else {
                                                flag_row = row + 1;
                                            }
                                            if (col > seleted_col) {
                                                flag_col = col - 1;
                                            } else {
                                                flag_col = col + 1;
                                            }
                                            CheckersBoard[flag_row][flag_col] = 0;
                                        }
                                            if(row == 0 && CheckersBoard[row][col] == 2)
                                        {
                                            CheckersBoard[row][col] = 4;
                                        }

                                        updateBoard();
                                        for (int i = 0; i < ROW_COL_COUNT; i++) {
                                            for (int j = 0; j < ROW_COL_COUNT; j++) {
                                                boxes[i][j].setClickable(false);
                                            }
                                        }
                                        turn = 1;
                                        selected_row = seleted_col = -1;
                                        performMove();

                                    }
                                }
                            }
                            else
                            {
                                if (CheckersBoard[row][col] == 2) {
                                    boxes[row][col].setImageDrawable(getResources().getDrawable(R.drawable.you2));
                                    seleted_col = col;
                                    selected_row = row;
                                }
                                if(CheckersBoard[row][col] == 4)
                                {
                                    boxes[row][col].setImageDrawable(getResources().getDrawable(R.drawable.you2k));
                                    seleted_col = col;
                                    selected_row = row;
                                }
                            }





                        //performMove();
                    }
                });
            }
        }
    }


    public int[][] CheckersBoard = new int[8][8];
    public final int ROW_COL_COUNT = 8;
    ImageView[][] boxes = new ImageView[ROW_COL_COUNT][ROW_COL_COUNT];

    public int[] minimax(int[][] current, int depth, int turn) {
        ArrayList<int[]> moves = GenerateMoves(turn, current);
        int bestScore = turn == 1 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int piece_to_move_row = -1;
        int piece_to_move_col = -1;
        int piece_to_move_target_row = -1;
        int piece_to_move_target_col = -1;
        if (depth == 0 || moves.isEmpty()) {
            bestScore = evaluateBoard(current, turn);
        } else {
            for (int[] move : moves) {
                int[][] tempBoard = CopyBoard(current);
                tempBoard[move[2]][move[3]] = tempBoard[move[0]][move[1]];
                tempBoard[move[0]][move[1]] = 0;
                if (turn == 1) {
                    currentScore = minimax(tempBoard, depth - 1, 2)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        piece_to_move_row = move[0];
                        piece_to_move_col = move[1];
                        piece_to_move_target_row = move[2];
                        piece_to_move_target_col = move[3];
                    }
                } else {
                    currentScore = minimax(tempBoard, depth - 1, 1)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        piece_to_move_row = move[0];
                        piece_to_move_col = move[1];
                        piece_to_move_target_row = move[2];
                        piece_to_move_target_col = move[3];
                    }
                }
            }
        }

        return new int[]{bestScore, piece_to_move_row, piece_to_move_col,
                piece_to_move_target_row, piece_to_move_target_col};
    }
    boolean just_count;
    public int evaluateBoard(int[][] current, int turn) {
        int eval_value = 0;
        int temp_row, temp_col = 0;
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for (int j = 0; j < ROW_COL_COUNT; j++) {
                if (current[i][j] == 1) {
                    eval_value++;
                } else if (current[i][j] == 2) {
                    eval_value--;
                } else if (current[i][j] == 3) {
                    eval_value += 2;
                } else if (current[i][j] == 4) {
                    eval_value -= 2;
                }
                /*if(turn == 1)
					return eval_value;*/

                if(win) {
                    double prob = Math.random();
                    if (prob > 0.5) {
                        eval_value += 5;
                    } else {
                        eval_value -= 5;
                    }
                }
                if(just_count) {
                    if (current[i][j] == 1 || current[i][j] == 3) {
                        temp_row = i + 1;
                        temp_col = j + 1;
                        if (temp_col < ROW_COL_COUNT
                                && temp_row < ROW_COL_COUNT
                                && (current[temp_row][temp_col] == 2 || current[temp_row][temp_col] == 4)
                                && temp_row + 1 < ROW_COL_COUNT
                                && temp_col + 1 < ROW_COL_COUNT
                                && current[temp_row + 1][temp_col + 1] == 0) {
                            eval_value += 2;
                        }
                        temp_col = j - 1;
                        if (temp_col < ROW_COL_COUNT
                                && temp_col >= 0
                                && temp_row < ROW_COL_COUNT
                                && (current[temp_row][temp_col] == 2 || current[temp_row][temp_col] == 4)
                                && temp_row + 1 < ROW_COL_COUNT
                                && temp_col - 1 < ROW_COL_COUNT
                                && temp_col - 1 >= 0
                                && current[temp_row + 1][temp_col - 1] == 0) {
                            eval_value += 2;
                        }
                        temp_row = i - 1;
                        temp_col = j - 1;
                        if (current[i][j] == 3
                                && temp_col < ROW_COL_COUNT
                                && temp_col >= 0
                                && temp_row < ROW_COL_COUNT
                                && temp_row >= 0
                                && (current[temp_row][temp_col] == 2 || current[temp_row][temp_col] == 4)
                                && temp_row - 1 < ROW_COL_COUNT
                                && temp_row - 1 >= 0
                                && temp_col - 1 < ROW_COL_COUNT
                                && temp_col - 1 >= 0
                                && current[temp_row - 1][temp_col - 1] == 0) {
                            eval_value += 3;
                        }
                        temp_col = j + 1;
                        if (current[i][j] == 3
                                && temp_col < ROW_COL_COUNT
                                && temp_col >= 0
                                && temp_row < ROW_COL_COUNT
                                && temp_row >= 0
                                && (current[temp_row][temp_col] == 2 || current[temp_row][temp_col] == 4)
                                && temp_row - 1 < ROW_COL_COUNT
                                && temp_row - 1 >= 0
                                && temp_col + 1 < ROW_COL_COUNT
                                && temp_col + 1 >= 0
                                && current[temp_row - 1][temp_col + 1] == 0) {
                            eval_value += 3;
                        }
                    }
                    if (current[i][j] == 2 || current[i][j] == 4) {
                        temp_row = i - 1;
                        temp_col = j + 1;
                        if (temp_col < ROW_COL_COUNT
                                && temp_row < ROW_COL_COUNT
                                && temp_row >= 0
                                && (current[temp_row][temp_col] == 1 || current[temp_row][temp_col] == 3)
                                && temp_row - 1 < ROW_COL_COUNT
                                && temp_row - 1 >= 0
                                && temp_col + 1 < ROW_COL_COUNT
                                && current[temp_row - 1][temp_col + 1] == 0) {
                            eval_value -= 2;
                        }
                        temp_col = j - 1;
                        if (temp_col < ROW_COL_COUNT
                                && temp_col >= 0
                                && temp_row < ROW_COL_COUNT
                                && temp_row >= 0
                                && (current[temp_row][temp_col] == 1 || current[temp_row][temp_col] == 3)
                                && temp_row - 1 < ROW_COL_COUNT
                                && temp_row - 1 >= 0
                                && temp_col - 1 < ROW_COL_COUNT
                                && temp_col - 1 >= 0
                                && current[temp_row - 1][temp_col - 1] == 0) {
                            eval_value -= 2;
                        }
                        temp_row = i + 1;
                        temp_col = j + 1;
                        if (current[i][j] == 4
                                && temp_col < ROW_COL_COUNT
                                && temp_col >= 0
                                && temp_row < ROW_COL_COUNT
                                && temp_row >= 0
                                && (current[temp_row][temp_col] == 1 || current[temp_row][temp_col] == 3)
                                && temp_row + 1 < ROW_COL_COUNT
                                && temp_row + 1 >= 0
                                && temp_col + 1 < ROW_COL_COUNT
                                && temp_col + 1 >= 0
                                && current[temp_row + 1][temp_col + 1] == 0) {
                            eval_value -= 3;
                        }
                        temp_col = j - 1;
                        if (current[i][j] == 4
                                && temp_col < ROW_COL_COUNT
                                && temp_col >= 0
                                && temp_row < ROW_COL_COUNT
                                && temp_row >= 0
                                && (current[temp_row][temp_col] == 1 || current[temp_row][temp_col] == 3)
                                && temp_row + 1 < ROW_COL_COUNT
                                && temp_row + 1 >= 0
                                && temp_col - 1 < ROW_COL_COUNT
                                && temp_col - 1 >= 0
                                && current[temp_row + 1][temp_col - 1] == 0) {
                            eval_value -= 3;
                        }
                    }
                }
            }

        }
        return eval_value;
    }

    public int pieceCount(int[][] current) {
        int count = 0;
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for (int j = 0; j < ROW_COL_COUNT; j++) {
                if (current[i][j] == 1 || current[i][j] == 2
                        || current[i][j] == 3 || current[i][j] == 4)
                    count++;
            }
        }
        return count;
    }

    public int[][] CopyBoard(int[][] current) {
        int[][] tempBoard = new int[8][8];
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for (int j = 0; j < ROW_COL_COUNT; j++) {
                tempBoard[i][j] = current[i][j];
            }
        }
        return tempBoard;
    }

    public  ArrayList<int[]> GenerateMoves(int turn, int[][] current) {
        int opp = 0;
        if (turn == 1) {
            opp = 2;
        } else {
            opp = 1;
        }
        int temp_row, temp_col;
        temp_row = temp_col = 0;
        ArrayList<int[]> moves = new ArrayList<>();
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for (int j = 0; j < ROW_COL_COUNT; j++) {
                if (turn == 1
                        && (current[i][j] == turn || current[i][j] == turn + 2)) {
                    temp_row = i + 1;
                    temp_col = j + 1;
                    if (temp_col < ROW_COL_COUNT && temp_row < ROW_COL_COUNT) {
                        if (current[temp_row][temp_col] == 0) {
                            moves.add(new int[]{i, j, temp_row, temp_col});
                        } else if (current[temp_row][temp_col] == opp
                                || current[temp_row][temp_col] == opp + 2) {
                            if (temp_row + 1 < ROW_COL_COUNT
                                    && temp_col + 1 < ROW_COL_COUNT
                                    && current[temp_row + 1][temp_col + 1] == 0) {
                                moves.add(new int[]{i, j, temp_row + 1,
                                        temp_col + 1});
                            }
                        }
                    }
                    temp_col = j - 1;
                    if ((temp_col < ROW_COL_COUNT && temp_col >= 0)
                            && (temp_row < ROW_COL_COUNT)) {
                        if (current[temp_row][temp_col] == 0) {
                            moves.add(new int[]{i, j, temp_row, temp_col});
                        } else if (current[temp_row][temp_col] == opp
                                || current[temp_row][temp_col] == opp + 2) {
                            if (temp_row + 1 < ROW_COL_COUNT
                                    && (temp_col - 1 < ROW_COL_COUNT && temp_col - 1 >= 0)
                                    && current[temp_row + 1][temp_col - 1] == 0) {
                                moves.add(new int[]{i, j, temp_row + 1,
                                        temp_col - 1});
                            }
                        }
                    }
                    if (current[i][j] == turn + 2) {
                        temp_row = i - 1;
                        temp_col = j + 1;
                        if (temp_col < ROW_COL_COUNT
                                && (temp_row < ROW_COL_COUNT && temp_row >= 0)) {
                            if (current[temp_row][temp_col] == 0) {
                                moves.add(new int[]{i, j, temp_row, temp_col});
                            } else if (current[temp_row][temp_col] == opp
                                    || current[temp_row][temp_col] == opp + 2) {
                                if ((temp_row - 1 < ROW_COL_COUNT && temp_row - 1 >= 0)
                                        && temp_col + 1 < ROW_COL_COUNT
                                        && current[temp_row - 1][temp_col + 1] == 0) {
                                    moves.add(new int[]{i, j, temp_row - 1,
                                            temp_col + 1});
                                }
                            }
                        }
                        temp_col = j - 1;
                        if ((temp_col < ROW_COL_COUNT && temp_col >= 0)
                                && (temp_row < ROW_COL_COUNT && temp_row >= 0)) {
                            if (current[temp_row][temp_col] == 0) {
                                moves.add(new int[]{i, j, temp_row, temp_col});
                            } else if (current[temp_row][temp_col] == opp
                                    || current[temp_row][temp_col] == opp + 2) {
                                if ((temp_row - 1 < ROW_COL_COUNT && temp_row - 1 >= 0)
                                        && (temp_col - 1 < ROW_COL_COUNT && temp_col - 1 >= 0)
                                        && current[temp_row - 1][temp_col - 1] == 0) {
                                    moves.add(new int[]{i, j, temp_row - 1,
                                            temp_col - 1});
                                }
                            }
                        }
                    }

                } else if (turn == 2
                        && (current[i][j] == turn || current[i][j] == turn + 2)) {
                    temp_row = i - 1;
                    temp_col = j + 1;
                    if (temp_col < ROW_COL_COUNT
                            && (temp_row < ROW_COL_COUNT && temp_row >= 0)) {
                        if (current[temp_row][temp_col] == 0) {
                            moves.add(new int[]{i, j, temp_row, temp_col});
                        } else if (current[temp_row][temp_col] == opp
                                || current[temp_row][temp_col] == opp + 2) {
                            if ((temp_row - 1 < ROW_COL_COUNT && temp_row - 1 >= 0)
                                    && temp_col + 1 < ROW_COL_COUNT
                                    && current[temp_row - 1][temp_col + 1] == 0) {
                                moves.add(new int[]{i, j, temp_row - 1,
                                        temp_col + 1});
                            }
                        }
                    }
                    temp_col = j - 1;
                    if ((temp_col < ROW_COL_COUNT && temp_col >= 0)
                            && (temp_row < ROW_COL_COUNT && temp_row >= 0)) {
                        if (current[temp_row][temp_col] == 0) {
                            moves.add(new int[]{i, j, temp_row, temp_col});
                        } else if (current[temp_row][temp_col] == opp
                                || current[temp_row][temp_col] == opp + 2) {
                            if ((temp_row - 1 < ROW_COL_COUNT && temp_row - 1 >= 0)
                                    && (temp_col - 1 < ROW_COL_COUNT && temp_col - 1 >= 0)
                                    && current[temp_row - 1][temp_col - 1] == 0) {
                                moves.add(new int[]{i, j, temp_row - 1,
                                        temp_col - 1});
                            }
                        }
                    }
                    if (current[i][j] == turn + 2) {
                        temp_row = i + 1;
                        temp_col = j + 1;
                        if (temp_col < ROW_COL_COUNT
                                && temp_row < ROW_COL_COUNT) {
                            if (current[temp_row][temp_col] == 0) {
                                moves.add(new int[]{i, j, temp_row, temp_col});
                            } else if (current[temp_row][temp_col] == opp
                                    || current[temp_row][temp_col] == opp + 2) {
                                if (temp_row + 1 < ROW_COL_COUNT
                                        && temp_col + 1 < ROW_COL_COUNT
                                        && current[temp_row + 1][temp_col + 1] == 0) {
                                    moves.add(new int[]{i, j, temp_row + 1,
                                            temp_col + 1});
                                }
                            }
                        }
                        temp_col = j - 1;
                        if ((temp_col < ROW_COL_COUNT && temp_col >= 0)
                                && (temp_row < ROW_COL_COUNT)) {
                            if (current[temp_row][temp_col] == 0) {
                                moves.add(new int[]{i, j, temp_row, temp_col});
                            } else if (current[temp_row][temp_col] == opp
                                    || current[temp_row][temp_col] == opp + 2) {
                                if (temp_row + 1 < ROW_COL_COUNT
                                        && (temp_col - 1 < ROW_COL_COUNT && temp_col - 1 >= 0)
                                        && current[temp_row + 1][temp_col - 1] == 0) {
                                    moves.add(new int[]{i, j, temp_row + 1,
                                            temp_col - 1});
                                }
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    public void initBoard() {

        int count = 1;
        System.out.println(ROW_COL_COUNT);
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            count = 1;
            for (int j = i % 2 == 0 ? 0 : ROW_COL_COUNT - 1; count % 9 != 0; ) {
                if (count % 2 != 0 && (i < 3 || i > 4)) {
                    if (i < 3)
                        CheckersBoard[i][j] = 1;
                    else
                        CheckersBoard[i][j] = 2;
                } else {
                    CheckersBoard[i][j] = 0;
                }
                count++;
                if (i % 2 == 0)
                    j++;
                else
                    j--;
            }
        }
    }

    public void updateBoard(){
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for(int j = 0; j < ROW_COL_COUNT ; j++)
            {
                if(CheckersBoard[i][j] == 1)
                {
                    boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.me1));
                }
                else if(CheckersBoard[i][j] == 2)
                {
                    boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.you1));
                }
                else if(CheckersBoard[i][j] == 3)
                {
                    boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.me1k));
                }
                else if(CheckersBoard[i][j] == 4)
                {
                    boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.you1k));
                }
                else if(CheckersBoard[i][j] == 0)
                {
                    if(i%2 == 0)
                    {
                        if(j%2 == 0)
                        {
                            boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.gray));
                        }
                        else
                        {
                            boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.black));
                        }
                    }
                    else
                    {
                        if(j%2 == 0)
                        {
                            boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.black));
                        }
                        else
                        {
                            boxes[i][j].setImageDrawable(getResources().getDrawable(R.drawable.gray));
                        }
                    }

                }
            }
        }
    }
    // int turn = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBoxes();
        initBoard();
        updateBoard();
        turn = 1;
        for (int i = 0; i < ROW_COL_COUNT; i++) {
            for (int j = 0; j < ROW_COL_COUNT; j++) {
                boxes[i][j].setClickable(false);
            }
        }
        performMove();

    }


    public void performMove()
    {
        if (!GenerateMoves(turn, CheckersBoard).isEmpty()) {
            int[] move = minimax(CheckersBoard, 4, turn);
            //   count++;
            System.out.println();
            System.out.println("Turn: " + turn);
            //  System.out.println("Count: " + count);
            CheckersBoard[move[3]][move[4]] = CheckersBoard[move[1]][move[2]];
            if (Math.abs(move[3] - move[1]) == 2
                    && Math.abs(move[4] - move[2]) == 2 && move[3] > -1) {
                int flag_row = 0, flag_col = 0;
                if (move[3] > move[1]) {
                    flag_row = move[3] - 1;
                } else {
                    flag_row = move[3] + 1;
                }
                if (move[4] > move[2]) {
                    flag_col = move[4] - 1;
                } else {
                    flag_col = move[4] + 1;
                }
                CheckersBoard[flag_row][flag_col] = 0;
            }
            CheckersBoard[move[1]][move[2]] = 0;
            if ((move[3] == 0 || move[3] == 7)
                    && (CheckersBoard[move[3]][move[4]] == 1 || CheckersBoard[move[3]][move[4]] == 2)) {
                CheckersBoard[move[3]][move[4]] += 2;
            }
            System.out.println("Piece Count: " + pieceCount(CheckersBoard));
            // System.out.println("Evaluation: " +
            // evaluateBoard(CheckersBoard));
            // PrintBoard();
            updateBoard();
            //turn = turn == 1 ? 2 : 1;
            turn = 2;
            for (int i = 0; i < ROW_COL_COUNT; i++) {
                for (int j = 0; j < ROW_COL_COUNT; j++) {
                    boxes[i][j].setClickable(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_win) {
            win = true;
            just_count = true;
            return true;
        }
        else if(id == R.id.action_fair)
        {
            win = false;
            just_count = true;
            return true;
        }
        else if(id == R.id.action_count)
        {
            just_count = false;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
