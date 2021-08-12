package com.belegend.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private ConstraintLayout mLayout;
    private boolean player1Turn = true;
    private int mRoundCounter;
    private int iPlayer1Points;
    private int iPlayer2Points;
    private TextView mPlayer1Tv;
    private TextView mPlayer2Tv;
    private Button changeLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocal();
        setContentView(R.layout.activity_main);
        initializeInputs();

    }

    private void initializeInputs() {
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle(getResources().getString(R.string.app_name));
        mPlayer1Tv = findViewById(R.id.player_1_Tv);
        mPlayer2Tv = findViewById(R.id.player_2_Tv);
        mLayout = findViewById(R.id.main_const_layout);
        changeLang = findViewById(R.id.change_lang_btn);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                Log.e(String.valueOf(resID), "This is Result ID : ");
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button mResetButton = findViewById(R.id.reset_btn);
        mResetButton.setOnClickListener(this);
        changeLang.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset_btn:
                iPlayer1Points = 0;
                iPlayer2Points = 0;
                mPlayer1Tv.setText("Player 1 : " + iPlayer1Points);
                mPlayer2Tv.setText("Player 2 : " + iPlayer2Points);
                resetBoard();
                break;
            case R.id.change_lang_btn:
                showChangeLangDialog();
                break;
        }
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        if (player1Turn) {
            ((Button) v).setText("X");
            Log.e("Hi", "Hello Code X");
        } else {
            ((Button) v).setText("O");
            Log.e("Hi", "Hello Code O");
        }
        mRoundCounter++;

        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (mRoundCounter == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private void showChangeLangDialog() {
        final String[] language = {"عربى", "English"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Choose Language");
        mBuilder.setSingleChoiceItems(language, -1, (dialog, which) -> {
            if(which==0){
                setLocaleLanguage(MainActivity.this,"ar");
                //recreate();
            }else if(which ==1){
                setLocaleLanguage(MainActivity.this,"en");
                //recreate();
            }
            dialog.dismiss();
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocaleLanguage(Activity activity,String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources= activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My Lang", language);
        editor.apply();

    }

    public void loadLocal() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My Lang", "");
        setLocaleLanguage(MainActivity.this,language);

    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }

    private void player1Wins() {
        iPlayer1Points++;
        Snackbar.make(mLayout, "Player 1 Wins", Snackbar.LENGTH_SHORT).show();
        mPlayer1Tv.setText("Player 1 : " + iPlayer1Points);
        mPlayer2Tv.setText("Player 2 : " + iPlayer2Points);
        resetBoard();
    }


    private void player2Wins() {
        iPlayer2Points++;
        Snackbar.make(mLayout, "Player 2 Wins", Snackbar.LENGTH_SHORT).show();
        mPlayer1Tv.setText("Player 1 : " + iPlayer1Points);
        mPlayer2Tv.setText("Player 2 : " + iPlayer2Points);
        resetBoard();
    }

    private void draw() {
        Snackbar.make(mLayout, "Draw!", Snackbar.LENGTH_SHORT).show();
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        mRoundCounter = 0;
        player1Turn = true;
    }

}