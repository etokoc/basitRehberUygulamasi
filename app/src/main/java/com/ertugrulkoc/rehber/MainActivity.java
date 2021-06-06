package com.ertugrulkoc.rehber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText kulAd, kulSifre;
    CheckBox checkBox;
    SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       kisiyiHatirlamaDurumu();

        Button girisYapButton = findViewById(R.id.girisYapButton);
        girisYapButton.setOnClickListener(this);
        checkBox = findViewById(R.id.checkBox);
        kulAd = findViewById(R.id.editKullaniciAd);
        kulSifre = findViewById(R.id.editKullaniciSifre);
        Button uyeOlButton = findViewById(R.id.kayitOlButton);
        uyeOlButton.setOnClickListener(this);
        intent = getIntent();
        String kisiAd = intent.getStringExtra("kulAd");
        if (kisiAd != null) {
            kulAd.setText(kisiAd);
        }

    }

    private void kisiyiHatirlamaDurumu() {
        sharedPreferences = getSharedPreferences("remember",MODE_PRIVATE);
        if (sharedPreferences.getBoolean("remember",false)){
            intent = new Intent(this.getApplicationContext(),Rehber_Activity.class);
            startActivity(intent);
            this.finish();
        }
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
        KayitOlActivity kayitOlActivity = new KayitOlActivity();
        kayitOlActivity.xmlVeriCek();
        String kullaniciAd = kayitOlActivity.getKullaniciAdCekilenVeri();
        String kullaniciSifre = kayitOlActivity.getKullaniciSifreCekilenVeri();
        String kullanici_ad_cekilen = String.valueOf(kulAd.getText());
        String kullanici_sifre_cekilen = String.valueOf(kulSifre.getText());
        if (kullaniciAd != null && kullaniciSifre != null && !kullaniciAd.equals("") && !kullaniciSifre.equals("")) {
            if (kullaniciAd.equals(kullanici_ad_cekilen) && kullaniciSifre.equals(kullanici_sifre_cekilen)) {
                checkBoxKontrolEt();
                intent = new Intent(this.getApplicationContext(), Rehber_Activity.class);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(MainActivity.this, "Bilgilerinizi Kontrol Ediniz !", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Lütfen Eksiksiz Giriniz !", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBoxKontrolEt() {
        if (checkBox.isChecked()) {
            sharedPreferences = getSharedPreferences("remember", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("remember", true).apply();
        }
    }

    private void kayitOl() {
        intent = new Intent(this.getApplicationContext(), KayitOlActivity.class);
        startActivity(intent);
        this.finish();
    }
}