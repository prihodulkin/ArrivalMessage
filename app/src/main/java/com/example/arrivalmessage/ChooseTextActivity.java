package com.example.arrivalmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ChooseTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_text);
        addListenerOnButton();
    }
    public void addListenerOnButton() {
        ImageButton back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ChooseTextActivity.this, SelectLocationActivity.class));
                    }
                }
        );
        ImageButton ready_btn = findViewById(R.id.ready_btn);
        ready_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ChooseTextActivity.this, FinishActivity.class));
                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
