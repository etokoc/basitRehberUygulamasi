package com.ertugrulkoc.rehber;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button girisYapButton = findViewById(R.id.girisYapButton);
        girisYapButton.setOnClickListener(this);

        Button uyeOlButton = findViewById(R.id.kayitOlButton);
        uyeOlButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.girisYapButton:
                girisYap();
                break;

            case R.id.kayitOlButton:
                kayitOl();
                break;
        }

    }

    private void girisYap() {
        intent = new Intent(this.getApplicationContext(), Rehber_Activity.class);
        startActivity(intent);
        this.finish();
    }

    private void kayitOl() {
        intent = new Intent(this.getApplicationContext(), KayitOlActivity.class);
        startActivity(intent);
        this.finish();
    }
}