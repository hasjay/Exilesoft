package com.exilesoft.braintrain.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        Intent intent = getIntent();
        int result = intent.getIntExtra("game.result",0);
        final TextView txtPoints = (TextView)findViewById(R.id.lblPoints);
        txtPoints.setText(result+"!");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GameHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
