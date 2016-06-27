package com.exilesoft.braintrain.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.exilesoft.braintrain.util.Commons;

public class GameHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem hint = menu.findItem(R.id.action_hint);
        hint.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hint:
                item.setChecked(true);
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

    public void newGame(View view){
        final Intent gameLevelIntent = new Intent(this, GameLevelActivity.class);
        startActivity(gameLevelIntent);
    }

    public void continueGame(View view){
        final SharedPreferences sharedPref = getSharedPreferences(Commons.GAME_STATE , Context.MODE_PRIVATE);
        boolean saveState = sharedPref.getBoolean("game.save.state", false);
        if(saveState){
            final Intent gamePlayIntent = new Intent(this, GamePlayActivity.class);
            final SharedPreferences.Editor editor = sharedPref.edit();
            gamePlayIntent.putExtra("game.lap", sharedPref.getInt("game.lap", 0));
            gamePlayIntent.putExtra("game.level", sharedPref.getInt("game.level", 1));
            gamePlayIntent.putExtra("game.totalPoints", sharedPref.getInt("game.totalPoints", 0));
            gamePlayIntent.putExtra("game.save.state", sharedPref.getBoolean("game.save.state", false));
            gamePlayIntent.putExtra("game.hint", sharedPref.getBoolean("game.hint", false));
            editor.clear();
            editor.commit();
            startActivity(gamePlayIntent);
        }else{
            Toast.makeText(this,"No saved game found",Toast.LENGTH_LONG).show();
        }
    }

    public void about(View view){
        showAbout();
    }

    public void exit(View view){
        finish();
    }

    private void showAbout(){
        final Intent gameAboutIntent = new Intent(this, AboutGameActivity.class);
        startActivity(gameAboutIntent);
    }

}
