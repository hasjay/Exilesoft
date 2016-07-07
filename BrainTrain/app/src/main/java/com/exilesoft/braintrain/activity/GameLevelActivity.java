package com.exilesoft.braintrain.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.exilesoft.braintrain.util.Level;

public class GameLevelActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean isHintEnable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_level);

        final Button btnNovice = (Button) findViewById(R.id.btnNovice);
        btnNovice.setOnClickListener(this);
        final Button btnEasy = (Button) findViewById(R.id.btnEasy);
        btnEasy.setOnClickListener(this);
        final Button btnMedium = (Button) findViewById(R.id.btnMedium);
        btnMedium.setOnClickListener(this);
        final Button btnGuru = (Button) findViewById(R.id.btnGuru);
        btnGuru.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNovice:
                playGame(Level.NOVICE);
                break;
            case R.id.btnEasy:
                playGame(Level.EASY);
                break;
            case R.id.btnMedium:
                playGame(Level.MEDIUM);
                break;
            case R.id.btnGuru:
                playGame(Level.GURU);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hint:
                item.setChecked(true);
                isHintEnable = item.isChecked();
                return true;
            case R.id.action_about:
                showAbout();
                return true;
            case R.id.action_close:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem close = menu.findItem(R.id.action_close);
        close.setVisible(false);
        return true;
    }

    private void playGame(Level level) {
        final Intent gamePlayIntent = new Intent(this, GamePlayActivity.class);
        gamePlayIntent.putExtra("game.level", level.getLevel());
        gamePlayIntent.putExtra("game.hint", isHintEnable);
        startActivity(gamePlayIntent);
    }

    private void showAbout(){
        final Intent gameAboutIntent = new Intent(this, AboutGameActivity.class);
        startActivity(gameAboutIntent);
    }

}
