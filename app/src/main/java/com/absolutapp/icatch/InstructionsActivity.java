package com.absolutapp.icatch;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        StoryDialog storyDialog = new StoryDialog();
//        Dialog d = storyDialog.getDia();
//        d.show();

//        DialogFragment dialog = new StoryDialog();
//        Bundle args = new Bundle();
//        //args.putString(YesNoDialog.ARG_TITLE, title);
//        //args.putString(YesNoDialog.ARG_MESSAGE, message);
//        int yesno = 0;
//        dialog.setArguments(args);
//        //dialog.setTargetFragment(this, yesno);
//        dialog.show(getFragmentManager(), "tag");

    }

    public void goToGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);

    }

}
