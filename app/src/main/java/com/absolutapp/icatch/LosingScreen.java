package com.absolutapp.icatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class LosingScreen extends Activity implements View.OnClickListener {

    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_losing_screen);

       menuButton = findViewById(R.id.menuButton);
       menuButton.setOnClickListener(this);

      // startActivity(new Intent(this, FailureService.class));
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
