package com.exilesoft.braintrain.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exilesoft.braintrain.logic.ExpressionBuilder;
import static com.exilesoft.braintrain.util.Commons.*;

public class GamePlayActivity extends AppCompatActivity implements View.OnClickListener{

    private final Button btn[] = new Button[13];

    private ExpressionBuilder expBuilder = null;
    private CountDownTimer gameTimer;
    private boolean isReadyToNextLap = false;
    private boolean isHintEnable = false;
    private int hintAttempts = 0;
    private long remainingTime = 10;

    //game status
    private int lap = 1;
    private int totalPoints = 0;
    private int level = 1;
    private boolean isPause = false;

    private TextView txtAnswer;
    private TextView txtExpression;
    private TextView txtTime;
    private TextView txtResult;
    private TextView txtLap;

    public GamePlayActivity(){
        gameTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished / 1000;
                txtTime.setText("Time Remaining : " + remainingTime);
            }

            public void onFinish() {
                startLap();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Intent intent = getIntent();

        level = intent.getIntExtra("game.level",1);
        lap = intent.getIntExtra("game.lap",1);
        totalPoints = intent.getIntExtra("game.totalPoints",0);
        isHintEnable = intent.getBooleanExtra("game.hint",false);
        expBuilder = new ExpressionBuilder(level);

        txtAnswer = (TextView)findViewById(R.id.lblAnswer);
        txtExpression = (TextView)findViewById(R.id.lblExpression);
        txtTime = (TextView)findViewById(R.id.lblTimer);
        txtResult = (TextView)findViewById(R.id.lblResult);
        txtLap = (TextView)findViewById(R.id.lblLap);

        txtLap.setText("Game Round : " + lap);

        btn[0] = (Button)findViewById(R.id.button0);
        btn[1] = (Button)findViewById(R.id.button1);
        btn[2] = (Button)findViewById(R.id.button2);
        btn[3] = (Button)findViewById(R.id.button3);
        btn[4] = (Button)findViewById(R.id.button4);
        btn[5] = (Button)findViewById(R.id.button5);
        btn[6] = (Button)findViewById(R.id.button6);
        btn[7] = (Button)findViewById(R.id.button7);
        btn[8] = (Button)findViewById(R.id.button8);
        btn[9] = (Button)findViewById(R.id.button9);
        btn[10] = (Button)findViewById(R.id.buttonDel);
        btn[11] = (Button)findViewById(R.id.buttonContinue);
        btn[12] = (Button)findViewById(R.id.buttonMinus);
        //register onClick event
        for(int i =0;i<13;i++){
            btn[i].setOnClickListener(this);
        }

    }

    @Override
    protected void onResume() {
        startLap();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        String val = txtAnswer.getText().toString();
        val = val.equals("?")?"":val;
        switch (v.getId()) {
            case R.id.button0:
                txtAnswer.setText(val + "0");
                break;
            case R.id.button1:
                txtAnswer.setText(val + "1");
                break;
            case R.id.button2:
                txtAnswer.setText(val + "2");
                break;
            case R.id.button3:
                txtAnswer.setText(val + "3");
                break;
            case R.id.button4:
                txtAnswer.setText(val + "4");
                break;
            case R.id.button5:
                txtAnswer.setText(val + "5");
                break;
            case R.id.button6:
                txtAnswer.setText(val + "6");
                break;
            case R.id.button7:
                txtAnswer.setText(val + "7");
                break;
            case R.id.button8:
                txtAnswer.setText(val + "8");
                break;
            case R.id.button9:
                txtAnswer.setText(val + "9");
                break;
            case R.id.buttonDel:
                if(!"".equals(val)) {
                    txtAnswer.setText(val.substring(0, val.length() - 1));
                }
                break;
            case R.id.buttonContinue:
                continueGame();
                break;
            case R.id.buttonMinus:
                txtAnswer.setText("-");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        stopLap();
        isPause = true;
        if(remainingTime != 0){
            --lap;
        }
    }

    @Override
    public void onBackPressed() {
        stopLap();
        isPause = true;
        if(remainingTime != 0){
            --lap;
        }
        saveGameState();
        Intent intent = new Intent(this, GameHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void continueGame(){
        if(isReadyToNextLap){
            startLap();
        }else{
            stopLap();
            final String tmpAns = txtAnswer.getText().toString();
            final int expRes = expBuilder.getExpressionResult();
            if(isHintEnable){
                hintAttempts++;
                if(hintAttempts == NO_OF_ATTEMPTS){
                    isReadyToNextLap = true;
                }
                if (validate(tmpAns)) {
                    int ans = Integer.parseInt(tmpAns);
                    if (expRes == ans) {
                        setCorrect();
                        totalPoints = totalPoints + Math.round(100f / (10 - remainingTime));
                        isReadyToNextLap = true;
                        hintAttempts = 0;
                    } else if(expRes > ans){
                        showAttempts();
                        setGrater();
                        startTimer();
                    }else{
                        showAttempts();
                        setLess();
                        startTimer();
                    }
                }else{
                    setWrong();
                    isReadyToNextLap = true;
                }
            }else {
                isReadyToNextLap = true;
                if (validate(tmpAns)) {
                    if (expRes == Integer.parseInt(tmpAns)) {
                        setCorrect();
                        totalPoints = totalPoints + Math.round(100f / (10 - remainingTime));
                    } else {
                        setWrong();
                    }
                }else{
                    setWrong();
                }
            }
        }
    }

    private void startLap(){
        hintAttempts = 0;
        if(lap > NO_OF_LAP){
            Log.v("GamePlayFragment","Total Points : " + totalPoints);
            showResult();
        }else {
            txtLap.setText("Game Round : " + lap);
            isReadyToNextLap = false;
            if(!isPause){
                txtExpression.setText(expBuilder.buildExpression());
                txtAnswer.setText("?");
            }
            isPause = false;
            txtResult.setText("");
            startTimer();
        }
        lap++;
    }

    private void stopLap(){
        gameTimer.cancel();
    }

    private void showResult(){
        stopLap();
        final Intent gameResultIntent = new Intent(this, GameResultActivity.class);
        gameResultIntent.putExtra("game.result", totalPoints);
        startActivity(gameResultIntent);
    }

    private void setCorrect(){
        txtResult.setTextColor(ContextCompat.getColor(this, R.color.colorCorrect));
        txtResult.setText(R.string.correct);
    }

    private void setWrong(){
        txtResult.setTextColor(ContextCompat.getColor(this, R.color.colorWrong));
        txtResult.setText(R.string.wrong);
    }

    private void setGrater(){
        txtAnswer.setText("?");
        txtResult.setTextColor(ContextCompat.getColor(this, R.color.colorWrong));
        txtResult.setText(R.string.grater);
    }

    private void setLess(){
        txtAnswer.setText("?");
        txtResult.setTextColor(ContextCompat.getColor(this, R.color.colorWrong));
        txtResult.setText(R.string.less);
    }

    private void startTimer(){
        gameTimer.cancel();
        gameTimer.start();
    }

    private void showAttempts(){
        Toast.makeText(this,"You have " + (NO_OF_ATTEMPTS - hintAttempts) + " more attempts!!!.",Toast.LENGTH_LONG).show();
    }

    private void saveGameState(){
        final SharedPreferences sharedPref = getSharedPreferences(GAME_STATE , Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("game.lap", lap);
        editor.putInt("game.totalPoints", totalPoints);
        editor.putInt("game.level", level);
        editor.putBoolean("game.save.state", true);
        editor.putBoolean("game.hint", isHintEnable);
        editor.commit();
    }
}
