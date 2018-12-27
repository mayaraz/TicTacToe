package com.mayapp.lenovo.tictactoe;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int ARRAY_LENGTH = 3;
    public static final String EMPTY_BUTTON_TAG = "EMPTY";
    public static final String X_BUTTON_TAG = "X";
    public static final String O_BUTTON_TAG = "O";

    private boolean player1Turn = true;

    private int roundCount = 0;

    private int player1Points = 0;
    private int player2Points = 0;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private Button dialogBoxContinueButton;
    private ImageView dialogBoxImageView;
    private TextView dialogBoxTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide(); // hide the title bar

        textViewPlayer1 = findViewById(R.id.tv_p1);
        textViewPlayer2 = findViewById(R.id.tv_p2);


        Button buttonReset = findViewById(R.id.bt_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        setButtonsOnBoard();

    }

    private View.OnClickListener buttonsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (((ImageButton) v).getDrawable() != null) {
                return;
            }

            if (player1Turn) {
                ((ImageButton) v).setImageResource(R.drawable.nahari);
                ((ImageButton) v).setTag(X_BUTTON_TAG);
            } else {
                ((ImageButton) v).setImageResource(R.drawable.michal);
                ((ImageButton) v).setTag(O_BUTTON_TAG);
            }

            roundCount++;

            if (checkForWin()) {
                callWinner();
            } else if (roundCount == 9) {
                draw();
            } else {
                player1Turn = !player1Turn;
            }

        }
    };

    private void draw() {
        // Show a Toast saying this is a draw
        Toast.makeText(this, "Its a Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private boolean checkForWin() {
        // return true if there is a winner, else return false
        int sumLine;

        //Defines an int 2D array represented the buttons status
        int[][] buttonsArray = new int[ARRAY_LENGTH][ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++){
            for(int j = 0; j < ARRAY_LENGTH; j++){
                if (get_button(i, j).getTag().toString().equals(EMPTY_BUTTON_TAG)){
                    buttonsArray[i][j] = 0;
                } else if (get_button(i, j).getTag().toString().equals(X_BUTTON_TAG)){
                    buttonsArray[i][j] = 1;
                } else {
                    buttonsArray[i][j] = -1;
                }
            }
        }

        //Checks rows
        for (int i = 0; i < ARRAY_LENGTH; i++){
            sumLine = 0;
            for (int j = 0; j < ARRAY_LENGTH; j++){
                sumLine += buttonsArray[i][j];
            }
            if (Math.abs(sumLine) == ARRAY_LENGTH){
                return true;
            }
        }
        //Checks columns
        for (int i = 0; i < ARRAY_LENGTH; i++){
            sumLine = 0;
            for (int j = 0; j < ARRAY_LENGTH; j++){
                sumLine += buttonsArray[j][i];
            }
            if (Math.abs(sumLine) == ARRAY_LENGTH){
                return true;
            }
        }
        //Check left diagonal
        sumLine = 0;
        for (int i = 0; i < ARRAY_LENGTH; i++){
            sumLine += buttonsArray[i][i];
        }
        if (Math.abs(sumLine) == ARRAY_LENGTH){
            return true;
        }
        //Checks right diagonal
        sumLine = 0;
        for (int i = 0; i < ARRAY_LENGTH; i++){
            sumLine += buttonsArray[i][ARRAY_LENGTH - (1 + i)];
        }
        if (Math.abs(sumLine) == ARRAY_LENGTH){
            return true;
        }
        return false;
    }

    private ImageButton get_button(int i, int j) {
        // Gets i index and j index and returns the button object of this indexes
        String buttonID = "bt_" + i + j;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        ImageButton button = findViewById(resID);
        return button;
    }

    private void setButtonsOnBoard(){
        // Sets the 'OnClickListener' method of all the buttons
        for (int i = 0; i < ARRAY_LENGTH; i++){
            for (int j = 0; j < ARRAY_LENGTH; j++){
                ImageButton button = get_button(i, j);
                button.setOnClickListener(buttonsOnClickListener);
            }
        }
    }

    private void resetBoard() {
        // Sets all the buttons to empty image and all the tags to 'EMPTY_BUTTON_TAG'
        ImageButton button;

        for (int i = 0; i < ARRAY_LENGTH; i++) {
            for (int j = 0; j < ARRAY_LENGTH; j++) {
                button = get_button(i, j);
                button.setImageResource(0);
                button.setTag(EMPTY_BUTTON_TAG);
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        // Sets the points to be 0 and call the methods 'updatePointsText' and 'resetBoard'
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    private void updatePointsText() {
        //Updates the texViews according to the player1Points and p2ayer1Points values
        String p1Points = getString(R.string.Player1) + player1Points;
        String p2Points = getString(R.string.Player2) + player2Points;
        textViewPlayer1.setText(p1Points);
        textViewPlayer2.setText(p2Points);
    }

    private void callWinner(){
        // Raise a dialog box with an image of the winner, update the points and reset the board
        final Dialog dialogBox = new Dialog(this);
        dialogBox.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBox.setContentView(R.layout.winner_dialog);

        dialogBoxImageView = dialogBox.findViewById(R.id.imv_win_dialog_box);
        dialogBoxTextView =  dialogBox.findViewById(R.id.tv_win_dialog_box);
        dialogBoxContinueButton = dialogBox.findViewById(R.id.bt_continue);

        dialogBoxContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBox.cancel();
            }
        });
        if (player1Turn){
            dialogBoxImageView.setImageResource(R.drawable.nahari_wins);
            dialogBoxTextView.setText(R.string.Player1Wins);
            player1Points++;
        }
        else{
            dialogBoxImageView.setImageResource(R.drawable.michal_wins);
            dialogBoxTextView.setText(R.string.Player2Wins);
            player2Points++;
        }

        updatePointsText();
        resetBoard();
        dialogBox.show();
    }
}
